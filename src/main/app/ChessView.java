import backend.pieces.ChessPiece;
import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.Serial;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static backend.pieces.ChessConstants.*;

public class ChessView extends JPanel implements MouseListener, MouseMotionListener {

   @Serial
   private static final long serialVersionUID = -2301877366965276449L;
   private final Map<String, Image> keyNameValueImage = new HashMap<>();
   private final ChessDelegate chessDelegate;
   private ChessPiece movingPiece;
   private Point movingPiecePoint;
   private int originX = -1;
   private int originY = -1;
   private int cellSide = -1;
   private int fromCol = -1;
   private int fromRow = -1;

   ChessView(ChessDelegate chessDelegate) {
      this.chessDelegate = chessDelegate;

      String[] imageNames = {
         W_PAWN, W_KNIGHT, W_BISHOP, W_ROOK, W_QUEEN, W_KING,
         B_PAWN, B_KNIGHT, B_BISHOP, B_ROOK, B_QUEEN, B_KING
      };
      for (String imgName : imageNames) {
         Image img = loadImage(imgName + ".png");
         keyNameValueImage.put(imgName, img);
      }
      addMouseListener(this);
      addMouseMotionListener(this);
   }

   @Override
   protected void paintChildren(Graphics g) {
      super.paintChildren(g);
      int smallerWindowSide = Math.min(getSize().width, getSize().height);
      cellSide = smallerWindowSide / 8;
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
            if (p != null && p != movingPiece) {
               drawImage(g2, col, row, p.getImageName());
            }
         }
      }
      if (movingPiece != null && movingPiecePoint != null) {
         Image img = keyNameValueImage.get(movingPiece.getImageName());
         g2.drawImage(img,
            movingPiecePoint.x - cellSide / 2,
            movingPiecePoint.y - cellSide / 2,
            cellSide,
            cellSide,
            null);
      }
   }

   private void drawImage(Graphics2D g2, int col, int row, String imageName) {
      Image img = keyNameValueImage.get(imageName);
      g2.drawImage(img,
         originX + 5 + col * cellSide,
         originY + 5 + (7 - row) * cellSide,
         cellSide - 10,
         cellSide - 10,
         null);
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
            drawSquare(g2, 2 * i, 2 * j, LIGHT);
            drawSquare(g2, 1 + 2 * i, 1 + 2 * j, LIGHT);
            drawSquare(g2, 1 + 2 * i, 2 * j, DARK);
            drawSquare(g2, 2 * i, 1 + 2 * j, DARK);
         }
      }
   }

   private void drawSquare(Graphics2D g2, int col, int row, Color color) {
      g2.setColor(color);
      g2.fillRect(
         originX + col * cellSide,
         originY + row * cellSide,
         cellSide, cellSide);
   }

   @Override
   public void mouseClicked(MouseEvent e) {

   }

   @Override
   public void mousePressed(MouseEvent e) {
      fromCol = (e.getPoint().x - originX) / cellSide;
      fromRow = 7 - (e.getPoint().y - originY) / cellSide;
      movingPiece = chessDelegate.pieceAt(fromCol, fromRow);
   }

   @Override
   public void mouseReleased(MouseEvent e) {
      int toCol = (e.getPoint().x - originX) / cellSide;
      int toRow = 7 - (e.getPoint().y - originY) / cellSide;
      chessDelegate.movePiece(fromCol, fromRow, toCol, toRow);
      movingPiece = null;
      movingPiecePoint = null;
   }

   @Override
   public void mouseEntered(MouseEvent e) {

   }

   @Override
   public void mouseExited(MouseEvent e) {

   }

   @Override
   public void mouseDragged(MouseEvent e) {
      movingPiecePoint = e.getPoint();
      repaint();
   }

   @Override
   public void mouseMoved(MouseEvent e) {

   }
}
