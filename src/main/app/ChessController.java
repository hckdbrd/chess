import backend.pieces.ChessPiece;

import javax.swing.*;

public class ChessController implements ChessDelegate {

   private final ChessModel chessModel = new ChessModel();

   ChessController() {
      chessModel.reset();

      JFrame frame = new JFrame("Chess - GAMBIT");
      frame.setSize(600,600);

      ChessView panel = new ChessView();
      panel.chessDelegate = this;
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
}
