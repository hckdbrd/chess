import backend.pieces.ChessPiece;
import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.Serial;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ChessView extends JPanel {

   @Serial
   private static final long serialVersionUID = -2301877366965276449L;
   ChessDelegate chessDelegate;

   double scaleFactor = 0.9d;
   int originX = -1;
   int originY = -1;
   int cellSide = -1;

   Map<String, Image> keyNameValueImage = new HashMap<>();

   public ChessView() {
      String[] imageNames = {
         "Pawn-white",
         "Knight-white",
         "Bishop-white",
         "Rook-white",
         "Queen-white",
         "King-white",
         "Pawn-black",
         "Knight-black",
         "Bishop-black",
         "Rook-black",
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

      int smaller = Math.min(getSize().width, getSize().height);
      cellSide = (int) (smaller * scaleFactor / 8);
      originX = (getSize().width - 8 * cellSide) / 2;
      originY = (getSize().height - 8 * cellSide) / 2;

      Graphics2D g2 = (Graphics2D) g;
      drawBoard(g2);

      drawPieces(g2);

   }

   private void drawPieces(Graphics2D g2) {
      for (int row = 0; row < 8; row++) {
         for (int col = 0; col < 8; col++) {
            ChessPiece p = chessDelegate.pieceAt(col, row);
            if (p != null) {
               drawImage(g2, col, row, p.imageName);
            }
         }
      }
   }

   private void drawImage(Graphics2D g2, int col, int row, String imageName) {
      Image img = keyNameValueImage.get(imageName);
      g2.drawImage(img, originX + 5 + col * cellSide, originY + 5 + row * cellSide, cellSide - 10, cellSide - 10, null);
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
