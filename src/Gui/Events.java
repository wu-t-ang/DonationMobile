package Gui;

import com.codename1.components.ImageViewer;
import com.codename1.components.ToastBar;
import com.codename1.io.rest.Rest;
import com.codename1.location.Location;
import com.codename1.location.LocationManager;
import com.codename1.notifications.LocalNotification;
import com.codename1.ui.*;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.util.Resources;
import Entities.Event;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.codename1.ui.Button;
import com.codename1.ui.ButtonGroup;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.FontImage;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.URLImage;

public class Events extends Form {
    private Resources theme;
    private Home homePage;
    private String searchText = "";
    private boolean joinedOnly = false;
    private Container categoryBtnsList;
    private ButtonGroup categoryBtnsGroup;
    private Container sortByContainer;
    private Label sortByText;
    private ComboBox<String> sortBy;
    private DateTimeFormatter dateFormatter;
    private Container eventsContainer;
    private ActionListener joinListener;
    private ActionListener leaveListener;

    public Events(Resources theme) {
        super("Events", BoxLayout.y());
        this.theme = theme;
        this.homePage = new Home(theme);
        this.getToolbar().setBackCommand("Back", e -> {
            homePage.showBack();
        });
        this.getToolbar().addSearchCommand(e -> {
            searchText = (String) e.getSource();
            feedEventsContainer();
        });
        categoryBtnsGroup = new ButtonGroup();


        categoryBtnsGroup.addActionListener(ev -> {
            feedEventsContainer();
        });
        sortByText = new Label("Sort by: ");
        sortBy = new ComboBox<>("None", "Date", "Distance", "Price");
        sortBy.addSelectionListener((int oldSelected, int newSelected) -> {
            if (sortBy.getSelectedItem().equals("Location") && LocationManager.getLocationManager().getStatus() != LocationManager.AVAILABLE) {
                ToastBar.Status toast = ToastBar.getInstance().createStatus();
                toast.setExpires(500);
                toast.setMessage("Location Service Is Unavailable, Please Enable It!");
                toast.show();
                sortBy.setSelectedIndex(oldSelected);
            }
            else if (oldSelected != newSelected) {
                feedEventsContainer();
            }
        });
        sortByContainer = new Container(new FlowLayout(RIGHT));
        sortByContainer.addAll(sortByText, sortBy);
        eventsContainer = new Container(BoxLayout.y());
        eventsContainer.setScrollableY(true);
        feedEventsContainer();
        this.add( eventsContainer);
    }

    public Container generateEventContainer(Event event) {
        Container eventContainer = new Container(BoxLayout.x());
        Button actionBtn = new Button();
        EventsDetails detailsPage = new EventsDetails(theme, (Events) this, event);
        NavigationCommand detailsCmd = new NavigationCommand("Details", FontImage.createMaterial(FontImage.MATERIAL_ADD_TO_QUEUE, actionBtn.getStyle()).toImage());
        detailsCmd.setNextForm(detailsPage);
        Button detailsBtn = new Button(detailsCmd);

        joinListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int responseCode = Rest
                        .post("http://localhost/donationWEB/web/app_dev.php/Donation/{idEv}")
                        .pathParam("id_event", event.getIdEv().toString())
                        .getAsString()
                        .getResponseCode();
                if (responseCode == 200) {
                    LocalNotification notification = new LocalNotification();
                    notification.setId(event.getIdEv().toString());
                    notification.setAlertTitle("Event Reminder");
                    notification.setAlertBody(event.getNameEv() + " will start in 1 hour");
                    notification.setAlertSound("/notification_sound_events.mp3");
                    Display.getInstance().scheduleLocalNotification(notification, System.currentTimeMillis() + event.getDateEv().getTime() - 3600000, LocalNotification.REPEAT_NONE);
                    ((Button) actionEvent.getComponent()).setText("Leave");
                    ((Button) actionEvent.getComponent()).removeActionListener(joinListener);
                    ((Button) actionEvent.getComponent()).addActionListener(leaveListener);
                }
            }

        };
        leaveListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int responseCode = Rest
                        .post("http://localhost/donationWeb/web/app_dev.php/Donation/{idEv}")
                        .pathParam("id_event", event.getIdEv().toString())
                        .getAsString().getResponseCode();

            }
        };

        Container rightColumn = new Container(BoxLayout.y());
        Container commandsRow = new Container(BoxLayout.x());
        commandsRow.addAll(actionBtn, detailsBtn);
        rightColumn.addAll(new Label(event.getNameEv()), commandsRow);
        eventContainer.addAll(new ImageViewer(URLImage.createCachedImage("ei", "http://localhost/donationWEB/web/img/events/" + event.getPoster(),  Image.createImage(160, 120), URLImage.FLAG_RESIZE_SCALE_TO_FILL)), rightColumn);
        //poster = new ImageViewer(URLImage.createCachedImage("ei", "http://localhost/donationWEB/web/img/events/" + event.getPoster(), Image.createImage(1280, 720), URLImage.FLAG_RESIZE_SCALE_TO_FILL));

        return eventContainer;

    }
    public List<Event> fetchEvents() {
        List<Event> events = fetchEventsData();

       if (sortBy.getSelectedItem().equals("Distance") && LocationManager.getLocationManager().getStatus() == LocationManager.AVAILABLE) {
            Location currLocation = LocationManager.getLocationManager().getCurrentLocationSync();
            events.sort((Event e1, Event e2) -> {
                Location e1Location = new Location(e1.getLocationLatitude(), e1.getLocationLongitude());
                Location e2Location = new Location(e2.getLocationLatitude(), e2.getLocationLongitude());
                return Double.compare(e1Location.getDistanceTo(currLocation), e2Location.getDistanceTo(currLocation));
            });
        }

        return events;
    }
    public void feedEventsContainer() {
        //System.out.println(fetchEvents());
        eventsContainer.removeAll();
        fetchEvents().stream().map(this::generateEventContainer).forEach(component -> {
            eventsContainer.add(component);
        });
        eventsContainer.revalidate();
    }
    public List<Event> fetchEventsData() {
        dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        List<Event> eventList = new ArrayList<>();
        ((List<Map<String, Object>>) Rest
            .get("http://localhost/donationWeb/web/app_dev.php/Donation/get")
            .acceptJson()
            .getAsJsonMap()
            .getResponseData()
            .get("root"))
            .forEach(item -> {
                Event event = new Event(
                        ((Integer) item.get("idEV")),
                        (String) item.get("nameEv"),
                        (Double) item.get("locationLongitude"),
                        (Double) item.get("locationLatitude"),
                        Date.from(ZonedDateTime.parse((String) item.get("dateEv"), dateFormatter).toInstant()),
                        (String) item.get("descriptionEv"),
                        (String) item.get("equipementEv"),
                        (String) item.get("poster"),
                        (String) item.get("typeEv")

                );
                eventList.add(event);
        });

        return eventList;
    }
}
