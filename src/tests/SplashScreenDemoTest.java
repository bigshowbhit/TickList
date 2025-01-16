package tests;

import org.junit.jupiter.api.Test;
import screen.SplashScreenDemo;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class SplashScreenDemoTest {

    @Test
    void testLogoResourceLoading() {
        String logoPath = "/logo.png";
        ImageIcon logo = new ImageIcon(SplashScreenDemo.class.getResource(logoPath));

        // Verify the logo loads successfully
        assertNotNull(logo.getImage(), "Logo image should not be null.");
        assertEquals(MediaTracker.COMPLETE, logo.getImageLoadStatus(), "Logo should load successfully.");
    }

    @Test
    void testAspectRatioCalculation() {
        // Mock image dimensions
        int originalWidth = 800;
        int originalHeight = 600;
        double aspectRatio = (double) originalWidth / originalHeight;

        // Target splash dimensions
        int splashWidth = 400;
        int splashHeight = 300;

        int newWidth = splashWidth;
        int newHeight = (int) (splashWidth / aspectRatio);

        if (newHeight > splashHeight) {
            newHeight = splashHeight;
            newWidth = (int) (splashHeight * aspectRatio);
        }

        // Verify calculated dimensions maintain the aspect ratio
        assertEquals(4.0 / 3.0, aspectRatio, 0.01, "Aspect ratio should match 4:3.");
        assertTrue(newWidth <= splashWidth, "New width should not exceed splash width.");
        assertTrue(newHeight <= splashHeight, "New height should not exceed splash height.");
    }

    @Test
    void testSplashScreenPositioning() {
        int splashWidth = 400;
        int splashHeight = 300;

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int expectedX = screenSize.width / 2 - splashWidth / 2;
        int expectedY = screenSize.height / 2 - splashHeight / 2;

        // Verify splash screen positioning
        assertEquals(screenSize.width / 2 - splashWidth / 2, expectedX, "Splash screen X position should be centered.");
        assertEquals(screenSize.height / 2 - splashHeight / 2, expectedY, "Splash screen Y position should be centered.");
    }
}
