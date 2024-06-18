module com.example.selectionofperformers {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.desktop;

    opens com.example.selectionofperformers to javafx.fxml;
    exports com.example.selectionofperformers;
    exports com.example.selectionofperformers.Presentation.Controllers;
    exports com.example.selectionofperformers.Presentation.Model;
    exports com.example.selectionofperformers.domain.DataBase;
    exports com.example.selectionofperformers.domain.Entity;



}