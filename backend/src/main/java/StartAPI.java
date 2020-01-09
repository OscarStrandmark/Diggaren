import controllers.AddToLibraryController;

import static spark.Spark.*;

public class StartAPI {
    public static void main(String[] args) {
        port(5050);

        AddToLibraryController addToLibraryController = new AddToLibraryController();
        path("/spotify", () -> {

            //Search endpoints
            path("/library", () -> {
                put("/addto", (req,res) -> addToLibraryController.addToLibrary(req.body()));
            });
        });
    }
}
