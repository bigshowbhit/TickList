package screen;

import App.TickListApp;

import javax.swing.*;
import java.awt.*;

public class SplashScreenDemo {
    public static void main(String[] args) {

        showSplashScreen();

        new TickListApp();
    }

    private static void showSplashScreen() {

        JWindow splashScreen = new JWindow();
        JPanel content = new JPanel(new BorderLayout());
        splashScreen.setContentPane(content);

        String logoPath = "/logo.png";
        ImageIcon logo = new ImageIcon(SplashScreenDemo.class.getResource(logoPath));

        // Check if the logo is loaded correctly
        if (logo.getImageLoadStatus() == MediaTracker.COMPLETE) {
            System.out.println("Logo loaded successfully!");
        } else {
            System.out.println("Failed to load logo.");
        }

        int splashWidth = 400;
        int splashHeight = 300;

        Image image = logo.getImage();
        double aspectRatio = (double) image.getWidth(null) / image.getHeight(null);
        //aspect ratio = width/height

        int newWidth = splashWidth;
        int newHeight = (int) (splashWidth / aspectRatio);

        if (newHeight > splashHeight) {
            newHeight = splashHeight;
            newWidth = (int) (splashHeight * aspectRatio);
        }

        Image resizedImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        logo = new ImageIcon(resizedImage);

        JLabel logoLabel = new JLabel(logo, JLabel.CENTER);
        content.add(logoLabel, BorderLayout.CENTER);

        //Adding footer
        JLabel footerLabel = new JLabel("Loading TickList...", JLabel.CENTER);
        footerLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        footerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        content.add(footerLabel, BorderLayout.SOUTH);

        //Setting the splash screen at centre according to the system screen size
        //Formula = x = screenWidth/2 - splashWidth/2
        //y = screenHeight/2 - splashHeight/2
        splashScreen.setSize(splashWidth, splashHeight);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        splashScreen.setLocation(screenSize.width / 2 - splashScreen.getWidth() / 2,
                screenSize.height / 2 - splashScreen.getHeight() / 2);

        // Make splash screen visible
        splashScreen.setVisible(true);

        // Wait for 3 seconds
        try {
            Thread.sleep(3000); //the main thread
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        splashScreen.dispose();
    }
}
