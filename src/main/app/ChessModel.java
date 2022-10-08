import backend.pieces.ChessConstants;
import backend.pieces.ChessPiece;
import backend.pieces.Player;
import backend.pieces.Rank;

import java.util.HashSet;
import java.util.Set;

public class ChessModel {
   private final Set<ChessPiece> piecesBox = new HashSet<>();

   private Player playerInTurn = Player.WHITE;

   void reset() {

      piecesBox.clear();

      for (int i = 0; i < 8; i++) {
         piecesBox.add(new ChessPiece(i, 1, Player.WHITE, Rank.PAWN, ChessConstants.W_PAWN));
         piecesBox.add(new ChessPiece(i, 6, Player.BLACK, Rank.PAWN, ChessConstants.B_PAWN));
      }

      for (int i = 0; i < 2; i++) {
         piecesBox.add(new ChessPiece(i * 7, 0, Player.WHITE, Rank.ROOK, ChessConstants.W_ROOK));
         piecesBox.add(new ChessPiece(i * 7, 7, Player.BLACK, Rank.ROOK, ChessConstants.B_ROOK));

         piecesBox.add(new ChessPiece(1 + i * 5, 0, Player.WHITE, Rank.KNIGHT, ChessConstants.W_KNIGHT));
         piecesBox.add(new ChessPiece(1 + i * 5, 7, Player.BLACK, Rank.KNIGHT, ChessConstants.B_KNIGHT));

         piecesBox.add(new ChessPiece(2 + i * 3, 0, Player.WHITE, Rank.BISHOP, ChessConstants.W_BISHOP));
         piecesBox.add(new ChessPiece(2 + i * 3, 7, Player.BLACK, Rank.BISHOP, ChessConstants.B_BISHOP));
      }

      piecesBox.add(new ChessPiece(3, 0, Player.WHITE, Rank.QUEEN, ChessConstants.W_QUEEN));
      piecesBox.add(new ChessPiece(3, 7, Player.BLACK, Rank.QUEEN, ChessConstants.B_QUEEN));

      piecesBox.add(new ChessPiece(4, 0, Player.WHITE, Rank.KING, ChessConstants.W_KING));
      piecesBox.add(new ChessPiece(4, 7, Player.BLACK, Rank.KING, ChessConstants.B_KING));

      playerInTurn = Player.WHITE;
   }

   ChessPiece pieceAt(int col, int row) {
      for (ChessPiece chessPiece : piecesBox) {
         if (chessPiece.getCol() == col && chessPiece.getRow() == row) {
            return chessPiece;
         }
      }
      return null;
   }

   public void movePiece(int fromCol, int fromRow, int toCol, int toRow) {
      ChessPiece candidate = pieceAt(fromCol, fromRow);
      if (candidate == null || candidate.getPlayer() != playerInTurn) {
         return;
      }

      ChessPiece target = pieceAt(toCol, toRow);
      if (target != null) {
         if (target.getPlayer() == candidate.getPlayer()) {
            return;
         } else {
            piecesBox.remove(target);
         }
      }
      piecesBox.remove(candidate);
      piecesBox.add(new ChessPiece(toCol, toRow, candidate.getPlayer(), candidate.getRank(), candidate.getImageName()));
      playerInTurn = playerInTurn == Player.WHITE ? Player.BLACK : Player.WHITE;
   }

   @Override
   public String toString() {
      StringBuilder desc = new StringBuilder();
      for (int row = 7; row >= 0; row--) {
         desc.append(row);
         for (int col = 0; col < 8; col++) {
            ChessPiece p = pieceAt(col, row);
            if (p == null) {
               desc.append(" .");
            } else {
               desc.append(" ");
               switch (p.getRank()) {
                  case PAWN -> desc.append(p.getPlayer() == Player.WHITE ? "p" : "P");
                  case BISHOP -> desc.append(p.getPlayer() == Player.WHITE ? "b" : "B");
                  case KNIGHT -> desc.append(p.getPlayer() == Player.WHITE ? "n" : "N");
                  case ROOK -> desc.append(p.getPlayer() == Player.WHITE ? "r" : "R");
                  case QUEEN -> desc.append(p.getPlayer() == Player.WHITE ? "q" : "Q");
                  case KING -> desc.append(p.getPlayer() == Player.WHITE ? "k" : "K");
               }
            }
         }
         desc.append("\n");
      }
      desc.append("  0 1 2 3 4 5 6 7");
      return desc.toString();
   }

}
