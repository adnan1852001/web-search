import java.io.File;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class App extends Application {    
    @Override
    public void start(Stage stage) throws Exception {

        TabPane tabPane = new TabPane();
        Tab newTab = new Tab("+");
        tabPane.getTabs().add(new MyTab());
        tabPane.getTabs().add(newTab);
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, ov, nv) -> {
            if(nv.equals(newTab)){
                MyTab myNewTab = new MyTab();
                tabPane.getTabs().add(tabPane.getTabs().size() -1, myNewTab);
                tabPane.getSelectionModel().select(myNewTab);
            }
        });
        Scene scene = new Scene(tabPane, 640, 480);
        stage.setScene(scene);
        stage.setTitle("my simple browser");
        stage.show();
    }
}


class MyTab extends Tab {
    MyTab() {
        File f = new File("./test.fxml");
        Parent fxmlParent;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(f.toURI().toURL());
            fxmlParent = fxmlLoader.load();
        } catch (Exception e) {
            System.out.println(e);
            fxmlParent = new VBox(); 
        }
        this.setContent(fxmlParent);
        VBox vBox = (VBox)(fxmlParent.getChildrenUnmodifiable().get(0));
        WebView view = (WebView)(vBox.getChildren().get(1));
        view.getEngine().titleProperty().addListener((s,o,n)-> {
            this.setText(n);
        });
    }
}