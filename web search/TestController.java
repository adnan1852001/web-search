import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;

public class TestController implements Initializable {
    ArrayList<WebView> views;

    @FXML
    WebView webView;

    @FXML
    TextField url;

    @FXML
    TextField search;

    @FXML
    ProgressBar progBar;
    
    @FXML
    Button back;
    
    @FXML
    Button next;
    
    WebHistory history;

    @Override
    public void initialize(java.net.URL location, ResourceBundle resources) {
        webView.getEngine().locationProperty().addListener((s)->{
            url.textProperty().set(webView.getEngine().locationProperty().getValue());
        });
        webView.getEngine().load("https://www.google.com");

        progBar.progressProperty().bind(webView.getEngine().getLoadWorker().progressProperty());
        progBar.visibleProperty().bind(
                Bindings.when(progBar.progressProperty().lessThan(0).or(progBar.progressProperty().isEqualTo(1)))
                        .then(false)
                        .otherwise(true));
        progBar.managedProperty().bind(progBar.visibleProperty());
        progBar.setMaxWidth(Double.MAX_VALUE);

        history = webView.getEngine().getHistory();
        
        back.setDisable(true);
        history.currentIndexProperty().addListener((x,w,a)->{
            if(a.intValue() != 0){
                back.setDisable(false);
            } else {
                back.setDisable(true);
            }
        });
        
        next.setDisable(true);
        history.currentIndexProperty().addListener((x, w, a) -> {
            if (a.intValue() != history.getEntries().size()-1) {
                next.setDisable(false);
            } else {
                next.setDisable(true);
            }
        });
        
        url.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                webView.getEngine().load(url.getText());
            }
        });

        search.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                webView.getEngine().load("https://www.google.com/search?q=" + search.getText());
            }
        });
    }

    @FXML
    public void goHome(){
        webView.getEngine().load("https://www.google.com");
    }

    @FXML
    public String goBack() {
        ObservableList<WebHistory.Entry> entryList = history.getEntries();
        int currentIndex = history.getCurrentIndex();
        Platform.runLater(new Runnable() {
            public void run() {
                history.go(-1);
            }
        });
        return entryList.get(currentIndex > 0 ? currentIndex - 1 : currentIndex).getUrl();
    }

    @FXML
    public String goForward() {
        ObservableList<WebHistory.Entry> entryList = history.getEntries();
        int currentIndex = history.getCurrentIndex();
        Platform.runLater(new Runnable() {
            public void run() {
                history.go(1);
            }
        });
        return entryList.get(currentIndex < entryList.size() - 1 ? currentIndex + 1 : currentIndex).getUrl();
    }
}