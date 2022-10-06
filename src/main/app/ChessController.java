import backend.pieces.ChessPiece;

import javax.swing.*;

public class ChessController implements ChessDelegate {

   private final ChessModel chessModel = new ChessModel();
   private ChessView panel;
   ChessController() {
      chessModel.reset();

      JFrame frame = new JFrame("Chess - GAMBIT");
      frame.setSize(600,600);

      panel = new ChessView(this);
      frame.add(panel);

      frame.setVisible(true);
      frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
   }
   public static void main(String[] args) {
      new ChessController();
   }

   @Override
   public ChessPiece pieceAt(int col, int row) {
      return chessModel.pieceAt(col, row);
   }

   @Override
   public void movePiece(int fromCol, int fromRow, int toCol, int toRow) {
      chessModel.movePiece(fromCol, fromRow, toCol, toRow);
      panel.repaint();
   }

}
