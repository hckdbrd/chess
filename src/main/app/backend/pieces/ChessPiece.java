package backend.pieces;

public class ChessPiece {
   public int col;
   public int row;
   public Player player;
   public Rank rank;
   public String imageName;

   public ChessPiece(int col, int row, Player player, Rank rank, String imageName) {
      super();
      this.col = col;
      this.row = row;
      this.player = player;
      this.rank = rank;
      this.imageName = imageName;
   }
}
