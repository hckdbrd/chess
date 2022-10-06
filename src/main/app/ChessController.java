import backend.pieces.ChessPiece;

import javax.swing.*;
import java.awt.*;

public class ChessController implements ChessDelegate {

   private final ChessModel chessModel = new ChessModel();
   private final ChessView chessBoardPanel;

   ChessController() {
      chessModel.reset();

      JFrame frame = new JFrame("Chess - GAMBIT");
      frame.setSize(600, 600);
      frame.setLocationRelativeTo(null);
      frame.setLayout(new BorderLayout());

      chessBoardPanel = new ChessView(this);
      frame.add(chessBoardPanel, BorderLayout.CENTER);

      JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
      JButton resetBtn = new JButton("Reset");
      resetBtn.addActionListener(e -> {
         chessModel.reset();
         chessBoardPanel.repaint();
      });
      buttonsPanel.add(resetBtn);
      frame.add(buttonsPanel, BorderLayout.PAGE_END);

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
      chessBoardPanel.repaint();
   }

}
