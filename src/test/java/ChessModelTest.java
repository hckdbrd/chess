import backend.pieces.Player;
import backend.pieces.Rank;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ChessModelTest {
   ChessModel chessModel = new ChessModel();

   @Test
   public void movePieceTest() {
      chessModel.reset();
      chessModel.movePiece(0, 1, 0, 2);
   }

   @Test
   public void PieceAtTest() {
      assertNull(chessModel.pieceAt(0, 0));
      chessModel.reset();
      assertNotNull(chessModel.pieceAt(0, 0));
      assertEquals(Player.WHITE, chessModel.pieceAt(0, 0).player);
      assertEquals(Rank.ROOK, chessModel.pieceAt(0, 0).rank);
   }

   @Test
   public void toStringTest() {
      assertTrue(chessModel.toString().contains("0 . . . . . . . ."));
      chessModel.reset();
      assertTrue(chessModel.toString().contains("7 R N B Q K B N R"));
      assertTrue(chessModel.toString().contains("6 P P P P P P P P"));
      assertTrue(chessModel.toString().contains("5 . . . . . . . ."));
      assertTrue(chessModel.toString().contains("4 . . . . . . . ."));
      assertTrue(chessModel.toString().contains("3 . . . . . . . ."));
      assertTrue(chessModel.toString().contains("2 . . . . . . . ."));
      assertTrue(chessModel.toString().contains("1 p p p p p p p p"));
      assertTrue(chessModel.toString().contains("0 r n b q k b n r"));
   }
}
