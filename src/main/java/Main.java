
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Choose your system");
        System.out.println("Enter 1 for {M/M/1}");
        System.out.println("Enter 2 for {M/M/C}");
        System.out.println("Enter 3 for {M/M/1/K}");
        System.out.println("Enter 4 for {M/M/C/K}");
        Scanner scan = new Scanner(System.in);
        int choose = scan.nextInt();
        double lambda, mu;
        System.out.println("Enter the arrival rate");
        lambda = scan.nextDouble();
        System.out.println("Enter the service rate");
        mu = scan.nextDouble();
        int num_sim;

        switch (choose) {
            case 1: {
                System.out.println("Enter number of simulation");
                num_sim = scan.nextInt();
                MM1 mm1 = new MM1(lambda, mu, num_sim);
                mm1.displayInformation();
            }
                break;
            case 2: {
                System.out.println("Enter number of simulation");
                num_sim = scan.nextInt();
                System.out.println("Enter number of severs");
                int c = scan.nextInt();
                MMC mmc = new MMC(lambda, mu, c, num_sim);
                mmc.displayInformation();
            }
            break;
            case 3: {
                System.out.println("Enter number of simulation");
                num_sim = scan.nextInt();
                System.out.println("Enter the capacity");
                int k = scan.nextInt();
                MM1K mm1k = new MM1K(lambda, mu, k, num_sim);
                mm1k.displayInformation();
            }
            break;
            case 4:{
                System.out.println("Enter number of simulation");
                num_sim = scan.nextInt();
                System.out.println("Enter number of severs");
                int c = scan.nextInt();
                System.out.println("Enter the capacity");
                int k = scan.nextInt();

                MMCK mmck=new MMCK(lambda,mu,c,k,num_sim);
                mmck.displayInformation();
            }
            break;
            default:
                System.out.println("Invalid input");
        }
    }
}