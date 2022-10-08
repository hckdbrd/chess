package backend.pieces;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChessPiece {
   private int col;
   private int row;
   private Player player;
   private Rank rank;
   private String imageName;
}
