/**
 * Created by vladimirtsvetkov on 20/10/14.
 */
public class Jacobi {
    Matrix A;
    Matrix B;
    Matrix D;
    Matrix L;
    Matrix U;
    public Jacobi(Matrix A, Matrix B) {
        this.A = A;
        this.B = B;
    }
    public  Jacobi(Matrix D, Matrix L, Matrix U, Matrix B) {
        this.D = D;
        this.L = L;
        this.U = U;
        this.B = B;
    }
    public void dsplit() {
        Matrix D = new Matrix(A.M, A.M);
        Matrix L = new Matrix(A.M, A.M);
        Matrix U = new Matrix(A.M, A.M);
        for(int i = 0; i < A.M; i++) {
            D.setElement(i, i, A.GetElement(i, i));
        }
        for(int i = 0; i < (A.M - 1); i++)
            for(int j = i + 1; j < A.N; j++)
                U.setElement(i, j, A.GetElement(i, j));
        for(int i = 0; i < (A.M - 1); i++)
            for(int j = i + 1; j < A.N; j++)
                L.setElement(j, i, A.GetElement(j, i));
        this.D = D;
        this.L = L.muldig(-1);
        this.U = U.muldig(-1);
        System.out.println("A");
        this.A.show();
        System.out.println("D");
        this.D.show();
        System.out.println("L");
        this.L.show();
        System.out.println("U");
        this.U.show();
        //System.out.println("Solution:");
        System.out.println();
    }
    public Matrix execute(int stepmax, double accur) {
        Matrix xk = new Matrix(D.M, 1);
        Matrix xk1 = new Matrix(D.M, 1);
        for(int i = 0; i < D.M; i++) {
            xk.setElement(i, 0, 0.0);
        }
        for(int step = 0; step < stepmax; step++) {
            Matrix Dmin1 = D.degMin1();
            Matrix UplusL = U.plus(L);
            xk.show();

            xk1 = Dmin1.times(UplusL.times(xk)).plus(Dmin1.times(B));

            xk = xk1;

            System.out.println("\n");
            Matrix nevyz = (B.minus(A.times(xk)));
            double norm = nevyz.norm2();
            if (norm < accur) {
                System.out.println("Congrats!!!!!! steps Jacobi:" + step + "\n");
                break;
            }
        }
        System.out.println("A:\n");
        A.show();
        System.out.println("B:\n");
        B.show();
        System.out.println("xk:\n");
        xk.show();
        System.out.println("must be like B:\n");
        A.times(xk).show();
        System.out.println("nevyazka:\n");

        (B.minus(A.times(xk))).show();
        System.out.println("\n");
        return xk1;
    }
}
