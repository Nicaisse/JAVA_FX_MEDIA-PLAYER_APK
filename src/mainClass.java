
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.application.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.*;
import javafx.stage.FileChooser.ExtensionFilter;

public class mainClass extends Application {

    Stage window;
    private Boolean table_vide = false, back_img = false, media_en_cours = false, mute = false;
    private int id, compteur = 0;
    private String nom, path;
    private Player player = new Player();
    private FileChooser file_chooser;
    private File fichier;
    private File dossier;
    private Media media;
    private MediaPlayer media_player;
    private MediaView media_view;
    private Popup colorPickerPop;

    public ArrayList<String> list = new ArrayList<>();
    BorderPane pane = new BorderPane();
    HBox hb_all_btn = new HBox(10);
   
    ;
    Slider progressBar = new Slider();
    Slider slider = new Slider();
    //BorderPane pane2 = new BorderPane();
    VBox vb = new VBox();
    HBox hb1 = new HBox(25);
    HBox hb_volume = new HBox();
    Button btnpause = new Button();
    Button btn_replay = new Button();
    Button btnNext = new Button();
    Button btnstop = new Button("");
    Button btnavant = new Button("");
    Button btnrec = new Button("");
    Button btnvol = new Button();
    Slider slidVol = new Slider();
    Button btnprevious = new Button();
    HBox hb_menu_bar = new HBox();
    int quantite = 0;
    VBox dispo = new VBox();
    MenuItem fullInter = new MenuItem("fullScreen Interface");
    Scene scene;
    @SuppressWarnings("unchecked")
    TableView<Player> table_view = new TableView();
    //creation du bar de menu
    MenuBar menuBar = new MenuBar();
    MenuItem maxInter = new MenuItem("Maximal Interface");
    ImageView image_back = new ImageView(new Image("Image/img13.jpg"));

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        launch(args);
    }

    void createWindow() {
        window.setTitle("BBC Media Player");
        window.setMaximized(false);

    }

    @Override
    public void start(Stage stage) throws Exception {
        window = stage;
        scene = new Scene(pane);
        window.setScene(scene);
        createWindow();
        background();
        CreationMenu();
        progressbar();
        creationBoutton();
        creation_footer();
        exitFullScreen();
        window.show();
    }

    void CreationMenu() {
        //creation du menu pour les media
        Menu file = new Menu("Media");
        //creation des options du menu media
        MenuItem openFile = new MenuItem("Open File");
        //ajout d'un event sur l'option open file
        openFile.setOnAction(e -> openFileMethode());
        //ajout d'une combinaison de clavier sur l'option
        openFile.setAccelerator(KeyCombination.valueOf("CTRL+O"));
        MenuItem openMultipleFile = new MenuItem("Open Multiple File");
        //ajout d'un evenement sur l'option open multiple file
        openMultipleFile.setOnAction(e -> openMultipleFileMethode());
        //ajout d'une combinaison de clavier sur l'option
        openMultipleFile.setAccelerator(KeyCombination.valueOf("CTRL+SHIFT+O"));

        MenuItem openFolder = new MenuItem("Open Folder");
        openFolder.setOnAction(folder -> openFolderMethode());

        Menu liste = new Menu("Liste");
        MenuItem createListe = new MenuItem("Creer_liste");
        createListe.setOnAction(eh -> createListMethode());
        MenuItem sauvegarde = new MenuItem("Sauvegarde");
        sauvegarde.setOnAction(eh -> saveListe());
        liste.getItems().addAll(createListe, sauvegarde);
        MenuItem quit = new MenuItem("Quit");
        //ajout d'une combinaison de clavier sur l'option
        quit.setAccelerator(KeyCombination.valueOf("CTRL+Q"));
        quit.setOnAction(eh -> {
            Platform.exit();
        });
        Menu help = new Menu("HELP");
        MenuItem aide = new MenuItem("Help");
        //ajout d'un evenement
        aide.setOnAction(eh -> {
            Alert aide1 = new Alert(AlertType.INFORMATION);
            aide1.setTitle("Help");
            aide1.setWidth(800);
            aide1.setHeight(500);
            aide1.setContentText("Welcom to BBc Media player Help\n  BBC Media Player est cree pour pouvoir jouer des pistes\n pour jouer les pistes(Audio/video)il est important de creer une liste d'abord apres avoir cree la liste vous pouvez cliquer sur le menu media pour executer votre action");
            aide1.showAndWait();

        });
        MenuItem about = new MenuItem("About");
        about.setOnAction(a->{
            
        Alert about1 = new Alert(AlertType.INFORMATION);
            about1.setTitle("Help");
            about1.setWidth(800);
            about1.setHeight(500);
            about1.setContentText("BBC est un media Player cree par un groupe ING en informatique(1-Bastien Jean-Ritchy 2-Fabien Stanley 3-Fleurimond Carine 4-Nicaisse Bryan 5-Orelien Nageline 6-Samuel Mathieu)");
                   about1.showAndWait();

        
        });
        //ajout des options dans le menu help
        help.getItems().addAll(aide, about);
        //ajout des options dans le menu media
        file.getItems().addAll(openFile, openMultipleFile, openFolder, quit);
        //creation du menu view
        Menu view = new Menu("View");
        //les sous menus du menu view
        //ajout d'une combinaison de clavier sur l'option playist

        //ajout d'une combinaison de clavier sur l'option maxInter
        maxInter.setAccelerator(KeyCombination.valueOf("CTRL+M"));
        //ajout d'un evenement sur l'option maxInter
        maxInter.setOnAction(max -> maximiser());

        //ajout d'une combinaison de clavier sur l'option fullscreen
        fullInter.setAccelerator(KeyCombination.valueOf("F"));
        //ajout d'un evenement sur l'option maxInter
        fullInter.setOnAction(max -> fullScreen());

        MenuItem changerArriere = new MenuItem("Changer l'arriere plan Interface");
        //ajout d'un evenement sur l'option changerArrier
        changerArriere.setOnAction(event -> change_arrierePlan());
        //ajout des options dans le menu view
        view.getItems().addAll(maxInter, fullInter, changerArriere);
        file.setStyle("-fx-text-fill-color:white;");
        //ajout des menus liste, file,view qui gere lles media dans la barre de menu
        menuBar.getMenus().addAll(liste, file, view, help);

        menuBar.setStyle("-fx-background-color: gray;");
        //ajout de la barre de menu en haut du borderpane ou pane
        pane.setTop(menuBar);

    }

    @SuppressWarnings("unchecked")
    public void creationTableView() {
        //creation d'une table qui contiendra les media 
        //creation des colonnes ou entete pour ajouter les player 
        TableColumn<Player, String> colonne_1 = new TableColumn<>("Id");
        TableColumn<Player, String> colonne_2 = new TableColumn<>("Nom");
        TableColumn<Player, String> colonne_3 = new TableColumn<>("Path");

        //ajout de la valeur dans les colonnes 
        //colonne 1 pour id
        colonne_1.setCellValueFactory(new PropertyValueFactory<>("id"));
        //colonne 2 pour le nom du media
        colonne_2.setCellValueFactory(new PropertyValueFactory<>("nom"));
        //colonne 3 pour le path
        colonne_3.setCellValueFactory(new PropertyValueFactory<>("path"));

        // ajout des colonnes dans la table view
        table_view.getColumns().addAll(colonne_1, colonne_2, colonne_3);
        // redimensionner la table vue en fonction de nombre de colonne necesaire
        table_view.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // ajout de la liste a gauche du border pane
        pane.setLeft(table_view);
        table_vide = true;
        table_view.setVisible(true);
    }

    private void createListMethode() {
        if (!table_vide) {
            creationTableView();
        }
    }

    void openFileMethode() {
        createListMethode();

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Audio/Video", "*.mp3", "*.mp4", "*.flv", "*.3gp", "*.wma", "*.wav", "*.ogg", "*.wmv", "*.avg", "*.avi"));

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            compteur++;
            id = compteur;
            nom = file.getName();
            path = file.getPath();
            mainClass c = new mainClass();
            c.creationTableView();
            table_view.getItems().add(new Player(id, nom, path));
            list.add(id + " ; " + nom + " ; " + path + " ; \n");
            quantite++;

            // Ajoutez le code de lecture de fichier ici
            // Utilisez les bibliothèques ou les composants appropriés pour lire le fichier multimédia
        }
    }

    void openMultipleFileMethode() {
        createListMethode();
        file_chooser = new FileChooser();
        file_chooser.getExtensionFilters().add(new ExtensionFilter("Audio/Video", "*.mp3", "*.mp4", "*.flv", "*.3gp", "*.wma", "*.wav", "*.ogg", "*.wmv", "*.avg", "*.avi"));
        List<File> select = file_chooser.showOpenMultipleDialog(null);
        if (select != null) {
            for (File f : select) {
                compteur++;
                id = compteur;
                nom = f.getName();
                path = f.getPath();
                table_view.getItems().add(new Player(id, nom, path));
                list.add(id + " ; " + nom + " ; " + path + " ; \n");
                quantite++;
            }
        }
    }

    void openFolderMethode() {
        createListMethode();
        DirectoryChooser directory = new DirectoryChooser();
        directory.setInitialDirectory(new File(System.getProperty("user.home")));

        File selectedFolder = directory.showDialog(null);
        if (selectedFolder != null) {

            File[] files = selectedFolder.listFiles();
            if (files != null) {

                for (File file : files) {
                    compteur++;
                    id = compteur;
                    nom = file.getName();
                    path = file.getAbsolutePath();
                    table_view.getItems().add(new Player(id, nom, path));
                    list.add((id + ";" + nom + ";" + path + ";\n"));
                    quantite++;
                }

            }
        }
    }

    void saveListe() {
        if (table_vide && quantite > 0) {
            String name, chemin;
            dossier = new File("Play media");
            if (!dossier.exists()) {
                dossier.mkdir();
            }
            file_chooser = new FileChooser();
            file_chooser.setInitialDirectory(dossier);
            file_chooser.getExtensionFilters().add(new ExtensionFilter("texte", "*.txt"));
            fichier = file_chooser.showSaveDialog(null);
            name = fichier.getName();
            chemin = fichier.getPath();
            File fic = new File(dossier + "//" + name);
            if (!fic.exists()) {
                try {
                    fic.createNewFile();
                } catch (IOException e) {
                }
            }
            try {
                FileWriter ecrire = new FileWriter(fichier, true);
                ecrire.write(list + "\n");
                ecrire.close();
            } catch (IOException ex) {
            }
        } else {
            message_erreur();
        }
    }

    void message_erreur() {
        Alert error = new Alert(AlertType.ERROR);
        error.setTitle("Erreur");
        error.setHeaderText("");
        error.setContentText("Avant de franchir cet etape,svp creer d'abord une liste!!!");
        error.showAndWait();
    }

    void creationBoutton() {
        ImageView image_pause = new ImageView("Image/pause.png");
        image_pause.setFitWidth(30);
        image_pause.setFitHeight(30);
        ImageView image_play = new ImageView("Image/play.png");
        image_play.setFitWidth(30);
        image_play.setFitHeight(30);
        btnpause.setGraphic(image_pause);
        btnpause.setOnAction(eh -> {
            if (media_en_cours) {
                media_player.pause();
                btnpause.setGraphic(image_play);
                Tooltip tool = new Tooltip();
                tool.setText("Play");
                btnpause.setTooltip(tool);
                media_en_cours = false;
            } else {
                media_player.play();
                btnpause.setGraphic(image_pause);
                Tooltip tool = new Tooltip();
                tool.setText("Pause");
                btnpause.setTooltip(tool);

                media_en_cours = true;
            }
        });

        ImageView image_replay = new ImageView("Image/replay.png");

        image_replay.setFitWidth(25);
        image_replay.setFitHeight(25);

        btn_replay.setGraphic(image_replay);

        Tooltip tp_btn_replay = new Tooltip();
        tp_btn_replay.setText("Replay");
        btn_replay.setTooltip(tp_btn_replay);
        btn_replay.setOnAction(o -> {
            if (media_en_cours) {
                media_player.stop();
            }
            media_player.seek(media_player.getStartTime());
            media_player.play();
        });

        ImageView image_previous = new ImageView("Image/previous_Media.png");
        
        image_previous.setFitWidth(30);
        image_previous.setFitHeight(30);
        Tooltip tp_btnprevious = new Tooltip("Previous media");
        btnprevious.setTooltip(tp_btnprevious);
        btnprevious.setGraphic(image_previous);
        btnprevious.setOnAction(eh -> {
            table_view.getSelectionModel().selectPrevious();
            player = table_view.getSelectionModel().getSelectedItem();
            fichier = new File(player.getPath());
            if (media_en_cours) {
                media_player.pause();
                media_en_cours = false;
            }
            if (!media_en_cours) {
                fichier = new File(player.getPath());
                media = new Media(fichier.toURI().toString());
                media_player = new MediaPlayer(media);
                media_view = new MediaView(media_player);
                pane.setCenter(media_view);
                media_view.setFitHeight(600);
                media_view.setFitWidth(1000);
                if (window.isFullScreen()) {
                    Pane pane3 = new Pane();
// Créez un VBox et un StackPane pour contenir le VBox et le MediaView
                    VBox vbox = new VBox(dispo);
                    StackPane stackPane = new StackPane();
                    table_view.setPrefWidth(200);
                    table_view.setPadding(new Insets(0));
                    media_view.fitWidthProperty().bind(pane.widthProperty());
                    media_view.fitHeightProperty().bind(pane.heightProperty());
// Ajoutez le MediaView au StackPane
                    stackPane.getChildren().add(media_view);
// Ajoutez le VBox au StackPane
                    vbox.setPadding(new Insets(600, 0, 0, 0));
                    stackPane.getChildren().add(vbox);
// Ajoutez le StackPane au pane2
                    pane3.getChildren().add(stackPane);
// Ajoutez le pane2 au centre du BorderPane
                    pane.setCenter(pane3);
                }
                media_player.play();
                //media_player.seek(javafx.util.Duration.seconds(progress.getValue()));
                deplacementProgressBar();
                media_en_cours = true;

            }
        });

        ImageView image_next = new ImageView("Image/next_Media.png");
        image_next.setFitWidth(30);
        image_next.setFitHeight(30);
        Tooltip tp_btnNext = new Tooltip("Next media");
        btnNext.setTooltip(tp_btnNext);
        btnNext.setGraphic(image_next);
        btnNext.setOnAction(eh -> {
            table_view.getSelectionModel().selectNext();
            player = table_view.getSelectionModel().getSelectedItem();
            fichier = new File(player.getPath());
            if (media_en_cours) {
                media_player.pause();
                media_en_cours = false;
            }
            if (!media_en_cours) {
                fichier = new File(player.getPath());
                media = new Media(fichier.toURI().toString());
                media_player = new MediaPlayer(media);
                media_view = new MediaView(media_player);
                pane.setCenter(media_view);
                media_view.setFitHeight(600);
                media_view.setFitWidth(1000);
                if (window.isFullScreen()) {
                    Pane pane3 = new Pane();
// Créez un VBox et un StackPane pour contenir le VBox et le MediaView
                    VBox vbox = new VBox(dispo);
                    StackPane stackPane = new StackPane();
                    table_view.setPrefWidth(200);
                    table_view.setPadding(new Insets(0));

                    media_view.fitWidthProperty().bind(pane.widthProperty());
                    media_view.fitHeightProperty().bind(pane.heightProperty());
// Ajoutez le MediaView au StackPane
                    stackPane.getChildren().add(media_view);
// Ajoutez le VBox au StackPane
                    vbox.setPadding(new Insets(600, 0, 0, 0));
                    stackPane.getChildren().add(vbox);
// Ajoutez le StackPane au pane2
                    pane3.getChildren().add(stackPane);
// Ajoutez le pane2 au centre du BorderPane
                    pane.setCenter(pane3);
                }
                media_player.play();
                //media_player.seek(javafx.util.Duration.seconds(progress.getValue()));
                deplacementProgressBar();
                media_en_cours = true;

            }
        });
        ImageView image_stop = new ImageView("Image/Icon_Principal.png");
        image_stop.setFitHeight(30);
        image_stop.setFitWidth(30);
        Tooltip tp_stop = new Tooltip("StopMedia");
        btnstop.setTooltip(tp_stop);

        btnstop.setGraphic(image_stop);
        btnstop.setOnAction(a -> {
            if (media_en_cours) {
                media_player.stop();
            }
        });
        ImageView image_avant = new ImageView("Image/recule.png");
        image_avant.setFitHeight(30);
        image_avant.setFitWidth(30);
        Tooltip tp_avant = new Tooltip("recule10s");
        btnavant.setTooltip(tp_avant);

        btnavant.setGraphic(image_avant);
        btnavant.setOnAction(eh -> {
            media_player.seek(media_player.getCurrentTime().add(javafx.util.Duration.seconds(-10)));

        });
        ImageView image_rec = new ImageView("Image/avancer.png");
        image_rec.setFitHeight(30);
        image_rec.setFitWidth(30);
        Tooltip tp_rec = new Tooltip("avance10s");
        btnrec.setTooltip(tp_rec);

        btnrec.setGraphic(image_rec);
        btnrec.setOnAction(eh -> {
            media_player.seek(media_player.getCurrentTime().add(javafx.util.Duration.seconds(10)));
        });
        ImageView image_volume = new ImageView("Image/volume.png");
        image_volume.setFitWidth(30);
        image_volume.setFitHeight(30);
        Button btnvol = new Button();
        btnvol.setGraphic(image_volume);
        ImageView image_mute = new ImageView("Image//Mute.png");
        image_mute.setFitHeight(40);
        image_mute.setFitWidth(40);
        btnvol.setOnAction(eh -> {
            if (!mute) {
                media_player.setMute(true);
                btnvol.setGraphic(image_mute);
                mute = true;
            } else {
                media_player.setMute(false);
                btnvol.setGraphic(image_volume);
                mute = false;
            }
        });

        slidVol.setPrefWidth(180);
        slidVol.setMaxWidth(Region.USE_PREF_SIZE);
        slidVol.setMinWidth(10);
        //slidVol.setValue(75);
        slidVol.setMin(0);
        slidVol.setMax(20);
        slidVol.setOnMouseClicked(event -> {
            media_player.volumeProperty().bind(slidVol.valueProperty().divide(5));
        });
        hb_volume.getChildren().addAll(btnvol, slidVol);
        hb1.getChildren().addAll(btnpause, btnprevious, btn_replay, btnavant, btnNext, btnrec, btnstop, hb_volume);
        vb.getChildren().add(hb1);
    }

    void progressbar() {
        table_view.setOnMouseClicked(eh -> {
            if (media_en_cours) {
                media_player.pause();
                media_en_cours = false;
            }
            player = table_view.getSelectionModel().getSelectedItem();
            if (!media_en_cours) {
                fichier = new File(player.getPath());
                media = new Media(fichier.toURI().toString());
                media_player = new MediaPlayer(media);
                //ici
                deplacementProgressBar();
                media_view = new MediaView(media_player);
                media_view.setFitHeight(600);
                media_view.setFitWidth(1000);
                media_player.setAutoPlay(true);
                pane.setCenter(media_view);
                media_en_cours = true;

            }
        });
    }

    void deplacementProgressBar() {
        Tooltip tooltip = new Tooltip();
        DoubleProperty progressProperty = new SimpleDoubleProperty(0.5);
        //progressBar.progressProperty().bind(progressProperty);
        media_player.currentTimeProperty().addListener((ObservableValue<? extends javafx.util.Duration> observable, javafx.util.Duration oldValue, javafx.util.Duration newValue) -> {
            progressBar.setValue(newValue.toSeconds());

        });
        progressBar.setOnMousePressed((MouseEvent event) -> {
            media_player.seek(javafx.util.Duration.seconds(progressBar.getValue()));
        });
        progressBar.setOnMouseDragged((MouseEvent event) -> {
            double prog;
            // double value = prog * 100;
            // tooltip.setText("Progress: " + String.format("%.2f", value) + "%");
            tooltip.show(progressBar, event.getScreenX(), event.getScreenY());

            media_player.seek(javafx.util.Duration.seconds(progressBar.getValue()));
        });
        media_player.setOnReady(() -> {
            javafx.util.Duration total_duration = media.getDuration();
            progressBar.setMax(total_duration.toSeconds());
        });

    }

    void creation_footer() {
        progressBar.setPadding(new Insets(0, 0, 0, 350));
        progressBar.setPrefWidth(1270);

        vb.getChildren().addAll(progressBar);
        vb.setAlignment(Pos.CENTER);
        hb_volume.setAlignment(Pos.CENTER);
        //hb_volume.setPadding(new Insets(0,0,0,95));

        hb_all_btn.getChildren().addAll(btn_replay, btnprevious, btnpause, btnNext, btnavant, btnstop, btnrec, hb_volume);

        hb_all_btn.setAlignment(Pos.CENTER);
        hb_all_btn.setPadding(new Insets(5, 0, 20, 0));
        hb_menu_bar.getChildren().addAll(vb);
        dispo.getChildren().addAll(hb_menu_bar, hb_all_btn);
        dispo.setStyle("-fx-background-color: gray;");
//dispo.setPadding(new Insets(10, 0, 100, 0));
        pane.setBottom(dispo);
    }

    //methode pour minimiser
    //methode pour maximiser
    void maximiser() {
        
        if (maxInter.getText().equals("Maximal Interface")) {
                        window.setMaximized(false);
media_view.setFitHeight(300);
media_view.setFitWidth(500);
        image_back.setFitHeight(300);
        image_back.setFitWidth(600);
            maxInter.setText("Minimal Interface");
        } else {
            media_view.setFitHeight(800);
media_view.setFitWidth(1000);

            image_back.setFitHeight(600);
        image_back.setFitWidth(1280);
            maxInter.setText("Maximal Interface");
                        window.setMaximized(true);
        }
    }
//methode plein d'ecran

    void fullScreen() {
        // MenuItem fullInter = new MenuItem("fullScreen Interface");
        if (fullInter.getText().equals("fullScreen Interface") && media_en_cours) {
            //fullInter.setText(nom);.equals("fullScreen Interface");
            fullInter.setText("exit fullScreen");
            pane.setTop(null);
            pane.setLeft(null);

            media_view.fitWidthProperty().bind(pane.widthProperty());
            media_view.fitHeightProperty().bind(pane.heightProperty());
            Pane pane2 = new Pane();
// Créez un VBox et un StackPane pour contenir le VBox et le MediaView
            VBox vbox = new VBox(dispo);
            StackPane stackPane = new StackPane();

// Ajoutez le MediaView au StackPane
            stackPane.getChildren().add(media_view);
// Ajoutez le VBox au StackPane
            vbox.setPadding(new Insets(600, 0, 0, 0));
            stackPane.getChildren().add(vbox);
// Ajoutez le StackPane au pane2
            pane2.getChildren().add(stackPane);
// Ajoutez le pane2 au centre du BorderPane
            pane.setCenter(pane2);
            window.setFullScreenExitHint("Pressez escape pour sortir");
            window.setFullScreen(true);
        } else {
            fullInter.setText("fullScreen Interface");
        }
    }

    void exitFullScreen() {
        scene.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            if (keyCode == KeyCode.ESCAPE && media_en_cours) {
                //media_player.pause();
                fullInter.setText("fullScreen Interface");
                //window.setFullScreen(false);
                media_view = new MediaView(media_player);
                media_view.setFitHeight(600);
                media_view.setFitWidth(1000);
                //media_player.setAutoPlay(true);
                pane.setTop(menuBar);
                pane.setLeft(table_view);
                pane.setCenter(media_view);
                media_view.fitWidthProperty().unbind();
                media_view.fitHeightProperty().unbind();
                pane.setBottom(dispo);

                // L'utilisateur a appuyé sur la touche Entrée
                // Votre code de traitement ici
            }
        });

    }

    void change_arrierePlan() {
        image_back.setVisible(false);
        if (colorPickerPop == null) {
            colorPickerPop = new Popup();
            colorPickerPop.setAutoHide(true);
            ColorPicker colorpicker = new ColorPicker();
            colorpicker.setOnAction(event -> {
                javafx.scene.paint.Color selectedColor = colorpicker.getValue();
                BackgroundFill backgroundfill = new BackgroundFill(selectedColor, null, null);
                Background back = new Background(backgroundfill);
                pane.setBackground(back);
            });
            colorPickerPop.getContent().add(colorpicker);
        }
        if (!colorPickerPop.isShowing()) {
            colorPickerPop.show(pane.getScene().getWindow());
        }
    }

    void background() {
//        image_back.fitWidthProperty().bind(window.widthProperty());
//        image_back.fitHeightProperty().bind(window.heightProperty());
//
       image_back.setFitHeight(600);
       image_back.setFitWidth(1280);
        Group background = new Group();
        background.getChildren().add(image_back);
        pane.setCenter(background);
    }

}
