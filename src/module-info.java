module tutor {
	requires java.desktop;
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.base;
	
	opens pk.lkarten.ui to javafx.graphics;
}