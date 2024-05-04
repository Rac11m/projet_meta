package inteface;

        import a_star.Astar;
        import bfs.BFS;
        import bso.BSO;
        import dfs.DFS;
        import ga.GA;
        import javafx.collections.FXCollections;
        import javafx.collections.ObservableList;
        import javafx.fxml.FXML;
        import javafx.fxml.FXMLLoader;
        import javafx.fxml.Initializable;
        import javafx.scene.Parent;
        import javafx.scene.Scene;
        import javafx.scene.control.*;
        import javafx.stage.DirectoryChooser;
        import javafx.stage.FileChooser;
        import javafx.stage.Stage;
        import tests.Experimentation;
        import util.Etat;
        import util.Objet;
        import util.Sac;

        import java.io.File;
        import java.io.IOException;
        import java.net.URL;
        import java.util.ResourceBundle;

public class MainPageController implements Initializable {

    @FXML
    private ChoiceBox<String> alg_select;

    @FXML
    private Spinner<Integer> nbr_sac_spinner;

    @FXML
    private Spinner<Integer> nbr_objet_spinner;

    @FXML
    private TextField filePathTextFieldS;

    @FXML
    private TextField filePathTextFieldO;


    @FXML
    private Button chooseFileButtonO;

    @FXML
    private Button chooseFileButtonS;

    @FXML
    private Button chooseFileButtonO1;

    @FXML
    private RadioButton generateFileRadioButton;
    @FXML
    private RadioButton selectFileRadioButton;

    @FXML
    private ToggleGroup csv_file;

    @FXML
    private Button btnStart;

    @FXML
    private Label p_sizeLabel;

    @FXML
    private Label p_crossLabel;

    @FXML
    private Label p_mutateLabel;

    @FXML
    private TextField p_size;

    @FXML
    private TextField p_cross;

    @FXML
    private TextField p_mutate;

    @FXML
    private Label bee_sizeLabel;

    @FXML
    private Label chances_maxLabel;

    @FXML
    private Label flipLabel;

    @FXML
    private TextField bc_size;

    @FXML
    private TextField flip;

    @FXML
    private TextField chances_max;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int minValue = 1;
        int maxValue = 100;

        // Create a list of values
        ObservableList<String> values = FXCollections.observableArrayList("BFS", "DFS", "A*", "GA", "BSO");

        // Set the values to the ChoiceBox
        alg_select.setItems(values);

        SpinnerValueFactory<Integer> valueSacSpinner =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(minValue, maxValue, minValue);
        SpinnerValueFactory<Integer> valueObjetSpinner =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(minValue, maxValue, minValue);

        // Set the SpinnerValueFactory to the Spinner
        nbr_sac_spinner.setValueFactory(valueSacSpinner);
        nbr_objet_spinner.setValueFactory(valueObjetSpinner);
            }

    public String getSelectedAlgorithm() {
        return alg_select.getValue();
    }


    public void getParams() {
        if (alg_select.getValue() == "GA") {
            p_size.setVisible(true);
            p_sizeLabel.setVisible(true);
            p_cross.setVisible(true);
            p_crossLabel.setVisible(true);
            p_mutate.setVisible(true);
            p_mutateLabel.setVisible(true);
            bee_sizeLabel.setVisible(false);
            chances_maxLabel.setVisible(false);
            flipLabel.setVisible(false);
            bc_size.setVisible(false);
            chances_max.setVisible(false);
            flip.setVisible(false);
        } else if (alg_select.getValue() == "BSO") {
            bee_sizeLabel.setVisible(true);
            chances_maxLabel.setVisible(true);
            flipLabel.setVisible(true);
            bc_size.setVisible(true);
            chances_max.setVisible(true);
            flip.setVisible(true);
            p_size.setVisible(false);
            p_sizeLabel.setVisible(false);
            p_cross.setVisible(false);
            p_crossLabel.setVisible(false);
            p_mutate.setVisible(false);
            p_mutateLabel.setVisible(false);
        } else {
            p_size.setVisible(false);
            p_sizeLabel.setVisible(false);
            p_cross.setVisible(false);
            p_crossLabel.setVisible(false);
            p_mutate.setVisible(false);
            p_mutateLabel.setVisible(false);
            bee_sizeLabel.setVisible(false);
            chances_maxLabel.setVisible(false);
            flipLabel.setVisible(false);
            bc_size.setVisible(false);
            chances_max.setVisible(false);
            flip.setVisible(false);
        }
    }

    @FXML
    private void execute() {
        String sacsFilePath = filePathTextFieldS.getText();
        String objetsFilePath = filePathTextFieldO.getText();
        String selectedAlgorithm = getSelectedAlgorithm();
        long temps_exec = 0;
        int val_mkp = 0;
        Object[] res = processExecution(sacsFilePath, objetsFilePath, selectedAlgorithm, temps_exec, val_mkp);

        try {
            Stage stage =new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ressources/Page2.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("ressources/application.css").toExternalForm());

//            Stage stage = (Stage) btnStart.getScene().getWindow();
            stage.setTitle("MKP!");
            stage.setScene(scene);

            SecondPageController secondPageController = fxmlLoader.getController();
            secondPageController.setEtat((Etat) res[0]);
            secondPageController.handleTableView();
            secondPageController.setAlg_select(selectedAlgorithm, (Long) res[1], (Integer) res[2]);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Passer les informations récupérées à une autre méthode pour traitement

    }

    // Méthode pour effectuer les opérations nécessaires avec les informations récupérées
    private Object[] processExecution(String sacsFilePath, String objetsFilePath, String selectedAlgorithm, long temps_exec, int val_mkp) {
        // Effectuer les opérations nécessaires avec les informations récupérées
        System.out.println("Chemin du fichier des sacs : " + sacsFilePath);
        System.out.println("Chemin du fichier des objets : " + objetsFilePath);
        System.out.println("Algorithme selectionne : " + selectedAlgorithm);
        Sac[] sacs = Experimentation.readSacCSV(sacsFilePath);
        Objet[] objets = Experimentation.readObjetCSV(objetsFilePath);
        Etat but = new Etat(sacs.length, objets.length);
        if (selectedAlgorithm.equals("BFS")) {
            System.out.println("BFS");
            long tempsDebut = System.nanoTime();
            but = BFS.bfs(but, sacs, objets);
            long tempsFin = System.nanoTime();
            temps_exec = tempsFin - tempsDebut;
            val_mkp = BFS.calcul_val(but, sacs, objets);
        } else if (selectedAlgorithm.equals("DFS")) {
            System.out.println("DFS");
            long tempsDebut = System.nanoTime();
            but = DFS.dfs(but, sacs, objets);
            long tempsFin = System.nanoTime();
            temps_exec = tempsFin - tempsDebut;
            val_mkp = DFS.calcul_val(but, sacs, objets);
        } else if (selectedAlgorithm.equals("GA")) {
            System.out.println("GA");
            long tempsDebut = System.nanoTime();
            int population_size = Integer.parseInt(p_size.getText());
            double p_crossover = Double.parseDouble(p_cross.getText());
            double p_mutation = Double.parseDouble(p_mutate.getText());
            int[][] mat_but = GA.ga(sacs, objets, population_size, p_crossover, p_mutation);
            but.setMatrice(mat_but);
             long tempsFin = System.nanoTime();
            temps_exec = tempsFin - tempsDebut;
            val_mkp = GA.calcul_val(but.getMatrice(), sacs, objets);
        } else if (selectedAlgorithm.equals("BSO")) {
            System.out.println("BSO");
            long tempsDebut = System.nanoTime();
            int beeColony_size = Integer.parseInt(bc_size.getText());
            int max_chances = Integer.parseInt(chances_max.getText());
            int flip_val = Integer.parseInt(flip.getText());
            int[][] mat_but = BSO.bso(sacs, objets, beeColony_size, max_chances, flip_val);
            but.setMatrice(mat_but);
            long tempsFin = System.nanoTime();
            temps_exec = tempsFin - tempsDebut;
            val_mkp = BSO.calcul_val(but.getMatrice(), sacs, objets);
        } else {
            System.out.println("A*");
            long tempsDebut = System.nanoTime();
            but = Astar.astar(but, sacs, objets);
            long tempsFin = System.nanoTime();
            temps_exec = tempsFin - tempsDebut;
            val_mkp = Astar.calcul_val(but, sacs, objets);
        }
        Object[] result = new Object[]{but, temps_exec, val_mkp};
        return result;
    }

    @FXML
    private void chooseFileS() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir un fichier CSV");

        // Filtre pour les fichiers CSV
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Fichiers CSV (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);

        // Afficher la boîte de dialogue de sélection de fichier
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        // Mettre à jour le champ de texte avec le chemin du fichier sélectionné
        if (selectedFile != null) {
            filePathTextFieldS.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    private void chooseFileO() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir un fichier CSV");

//      Filtre pour les fichiers CSV
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Fichiers CSV (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);

//      Afficher la boîte de dialogue de sélection de fichier
        File selectedFile = fileChooser.showOpenDialog(new Stage());

//      Mettre à jour le champ de texte avec le chemin du fichier sélectionné
        if (selectedFile != null) {
            filePathTextFieldO.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    private void chooseFileG() {
        int nbr_sac = nbr_sac_spinner.getValue();
        int nbr_obj = nbr_objet_spinner.getValue();

        String sac_path = "tests/filesCSV/gen_files/sacs" + String.valueOf(nbr_sac) + "_" + String.valueOf(nbr_obj) + ".csv";
        String objet_path = "tests/filesCSV/gen_files/objets" + String.valueOf(nbr_sac) + "_" + String.valueOf(nbr_obj) + ".csv";

        Experimentation.genererDonneesTest(nbr_sac, nbr_obj, sac_path, objet_path);

        //////////////

        filePathTextFieldO.setText(objet_path);
        filePathTextFieldS.setText(sac_path);
    }

    @FXML
    private void handleGenerateFileRadioButton() {
        boolean isGenerateFileSelected = generateFileRadioButton.isSelected();
        // Désactivez les éléments si le bouton "Générer un fichier" est sélectionné
        filePathTextFieldO.setDisable(isGenerateFileSelected);
        chooseFileButtonO.setDisable(isGenerateFileSelected);
        filePathTextFieldS.setDisable(isGenerateFileSelected);
        chooseFileButtonS.setDisable(isGenerateFileSelected);
        filePathTextFieldS.clear();
        filePathTextFieldO.clear();
        nbr_sac_spinner.setDisable(false);
        nbr_objet_spinner.setDisable(false);
        chooseFileButtonO1.setDisable(false);
    }
    @FXML
    private void handleselectFileRadioButton() {
        boolean isSelectFileSelected = selectFileRadioButton.isSelected();
        // Désactivez les éléments si le bouton "Générer un fichier" est sélectionné
        nbr_sac_spinner.setDisable(isSelectFileSelected);
        nbr_objet_spinner.setDisable(isSelectFileSelected);
        chooseFileButtonO1.setDisable(isSelectFileSelected);
        nbr_sac_spinner.cancelEdit();
        nbr_objet_spinner.cancelEdit();
        filePathTextFieldO.setDisable(false);
        chooseFileButtonO.setDisable(false);
        filePathTextFieldS.setDisable(false);
        chooseFileButtonS.setDisable(false);
    }
}

