/**
 * Created by vladimirtsvetkov on 20/10/14.
 */
public class Cheb {
    Matrix A;
    Matrix B;
    Matrix D;
    Matrix L;
    Matrix U;
    double m;
    double M;
    public Cheb(Matrix A, Matrix B, double m, double M) {
        this.A = A;
        this.B = B;
        this.m = m;
        this.M = M;
    }
    public  Cheb(Matrix D, Matrix L, Matrix U, Matrix B) {
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
        System.out.println("Solution:");
    }
    public Matrix execute(int stepmax) {

        Matrix xk = new Matrix(D.M, 1);
        Matrix xk1 = new Matrix(D.M, 1);
        Matrix xkmin1 = new Matrix(D.M, 1);
        for(int i = 0; i < D.M; i++) {
            xk1.setElement(i, 0, 1.0);
        }
        double r0 = 1.0;
        boolean flag = false;
        for(int step = 0; step < stepmax; step++) {
            Matrix Dmin1 = D.degMin1();
            Matrix UplusL = U.plus(L);
            Matrix R = Dmin1.times(UplusL);
            Matrix C = Dmin1.times(B);

            double g = 2.0 / (2.0 - this.M - this.m);
            double sigm = (this.M - this.m) / (2.0 - this.M - this.m);
            double r1;
            if (!flag) {
                r1 = 1/(1 - 0.5 * sigm * sigm);
                flag = true;
            } else {
                r1 = 1/(1 - 0.25 * sigm * sigm *r0);
            }

            Matrix v1 = R.times(xk).plus(C);
            Matrix gv1 = v1.muldig(g);
            Matrix v2 = xk.muldig(1.0 - g);
            Matrix v3 = gv1.plus(v2).muldig(r1);
            Matrix v4 = xkmin1.muldig(1 - r1);
            xk1 = v3.plus(v4);
            //compute xk+1 here

            //xk1 = Rg.times(xk).plus(Cg);
            //xk1 = R.times(xk).plus(C);
            xk = xk1;
            r0 = r1;
            xkmin1 = xk;
            xk = xk1;
            xk1.show();
            System.out.println("\n");
            Matrix nevyz = (B.minus(A.times(xk)));
            double norm = nevyz.norm2();
            if (norm < 0.0001) {
                System.out.println("Congrats!!!!!! steps cheb:" + step + "\n");
                break;
            }
        }

        return xk1;

        //return null;
    }
}
