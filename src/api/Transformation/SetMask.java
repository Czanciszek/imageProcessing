package api.Transformation;

public class SetMask {

    static int[][] setMask(int length, int deg) {
        int[][] mask = new int[length][length];

        switch(deg) {
            case 0:
                for(int i = 0; i < length; i++) {
                    for(int j = 0; j<length; j++) {
                        if( i == length/2 )
                            mask[i][j] = 1;
                    }
                }
                break;
            case 45:
                for(int i = 0; i < length; i++) {
                    for(int j = 0; j<length; j++) {
                        if( (i+j) == (length-1) )
                            mask[i][j] = 1;
                    }
                }
                break;
            case 90:
                for(int i = 0; i < length; i++) {
                    for(int j = 0; j<length; j++) {
                        if( j == length/2 )
                            mask[i][j] = 1;
                    }
                }
                break;
            case 135:
                for(int i = 0; i < length; i++) {
                    for(int j = 0; j<length; j++) {
                        if( i == j )
                            mask[i][j] = 1;
                    }
                }
                break;
        }
        /*
        for( int tx = 0; tx<length; tx++ ) {
            for( int ty = 0; ty<length; ty++ ) {
                System.out.print(mask[tx][ty]);
            }
            System.out.println();
        }
        */
        return mask;
    }
}
