import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ChessPanel extends JPanel {

   int originX = 55;
   int originY = 45;
   int cellSide = 60;

   Map<String, Image> keyNameValueImage = new HashMap<>();

   public ChessPanel() {
      String[] imageNames = {
         "Pawn-white",
         "Knight-white",
         "Bishop-white",
         "Queen-white",
         "King-white",
         "Pawn-black",
         "Knight-black",
         "Bishop-black",
         "Queen-black",
         "King-black"
      };
      for (String imgName : imageNames) {
         Image img = loadImage(imgName + ".png");
         keyNameValueImage.put(imgName, img);
      }
   }

   Color light = Color.decode("#E8EDF9");
   Color dark = Color.decode("#B7C0D8");

   @Override
   protected void paintChildren(Graphics g) {
      super.paintChildren(g);

      Graphics2D g2 = (Graphics2D) g;
      drawBoard(g2);
      drawImage(g2, 0,0, "Pawn-black");

   }

   private void drawImage(Graphics2D g2, int col, int row, String imgName) {
      Image img = keyNameValueImage.get("Bishop-white");
      g2.drawImage(img, originX+5 + col * cellSide, originY+5 + row * cellSide, cellSide-10, cellSide-10, null);
   }

   @SneakyThrows
   private Image loadImage(String imgFileName) {
      ClassLoader classLoader = getClass().getClassLoader();
      URL resURL = classLoader.getResource("pieces/" + imgFileName);
      if (resURL == null) {
         System.out.println("No image found.");
         return null;
      } else {
         File imgFile = new File(resURL.toURI());
         return ImageIO.read(imgFile);
      }
   }

   private void drawBoard(Graphics2D g2) {
      for (int j = 0; j < 4; j++) {
         for (int i = 0; i < 4; i++) {
            drawSquare(g2, 2 * i, 2 * j, light);
            drawSquare(g2, 1 + 2 * i, 1 + 2 * j, light);
            drawSquare(g2, 1 + 2 * i, 2 * j, dark);
            drawSquare(g2, 2 * i, 1 + 2 * j, dark);
         }
      }
   }

   private void drawSquare(Graphics2D g2, int col, int row, Color color) {
      g2.setColor(color);
      g2.fillRect(originX + col * cellSide, originY + row * cellSide, cellSide, cellSide);
   }
}
