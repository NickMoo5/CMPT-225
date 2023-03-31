package fifteenpuzzle;

public class unitTesting {

    public static void testDeepCopy() {
        int[][] arr = {{2,2},{2,2}};
        Puzzle puz = new Puzzle(arr, 2, 2);
        int[][] newArr = puz.getBoardDeepCopy();
        newArr[0][0] = 1;

        Puzzle newPuz = new Puzzle(newArr, 2, 2);

        if (puz.getBoard() == newArr) {
            System.out.println("Test Deep Copy Error 1");
        } else {
            System.out.println("Test Deep Copy OK 1");
        }

        if (puz.getBoard().equals(newArr)) {
            System.out.println("Test Deep Copy Error 2");
        } else {
            System.out.println("Test Deep Copy OK 2");
        }

        System.out.println("Original puzzle");
        System.out.println(puz);
        System.out.println("New PUzzle");
        System.out.println(newPuz);

    }
    public static void main(String[] args) {
        testDeepCopy();
    }
}
