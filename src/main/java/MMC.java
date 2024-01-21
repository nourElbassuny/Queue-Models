import org.jfree.ui.RefineryUtilities;

import java.util.*;

public class MMC {
    private static double lamda;
    private static double mu;
    private static int c;
    private static int num_simulation;
    private static double r;  // lamda/mu
    private static double ru; //r/c

    public MMC(double lamda, double mu, int c, int num_simulation) {
        MMC.lamda = lamda;
        MMC.mu = mu;
        MMC.c = c;
        MMC.num_simulation = num_simulation;
        r = lamda / mu;
        ru = r / c;
    }

    private int fact(int n) {
        int res = 1;
        for (int i = 2; i <= n; i++) {
            res *= i;
        }
        return res;
    }

    private double P0() {
        // first case
        if (ru < 1) {
            double part1 = 0;
            for (int n = 0; n < c; n++) {
                part1 += (Math.pow(r, n) / this.fact(n));
            }
            double part2 = (c * Math.pow(r, c))
                    / ((this.fact(c)) * (c - r));
            return 1 / (part1 + part2);
        }
        // second case
        double part1 = 0;
        for (int n = 0; n < c; n++) {
            part1 += ((1.0 / this.fact(n)) * Math.pow(r, n));
        }
        double part2 = (1.0 / this.fact(c)) * Math.pow(r, c) * ((c * mu) / (c * mu - lamda));
        return 1 / (part1 + part2);
    }

    private double Pn(int n) {
        if (n >= 0 && n < c) {
            return ((Math.pow(lamda, n))
                    / (this.fact(n) * Math.pow(lamda, n))) * this.P0();
        }
        return ((Math.pow(lamda, n))
                / (Math.pow(c, n - c) * this.fact(c) * Math.pow(lamda, n))) * this.P0();
    }

    private double customerInQueue() {
        double numerator = Math.pow(r, c + 1) / c;
        double denominator = this.fact(c) * Math.pow((1 - r) / c, 2);
        return (numerator / denominator) * this.P0();

    }

    private double customerInSystem() {
        return this.customerInQueue() + r;
    }

    private double timeInQueue() {
        return this.customerInQueue() / lamda;
    }

    private double timeInSystem() {
        return (this.customerInQueue() / lamda) + (1 / mu);
    }

    public void displayInformation() {
        System.out.printf("%s%.4f\n", "L = ", customerInSystem());
        System.out.printf("%s%.4f\n", "Lq = ", customerInQueue());
        System.out.printf("%s%.4f\n", "W = ", timeInSystem());
        System.out.printf("%s%.4f\n", "Wq = ", timeInQueue());
        MMCSimulation simulation = new MMCSimulation();
        simulation.process();
        drawingSystem();
    }

    private void drawingSystem() {
        MMC.MMCSimulation simulationMMC = new MMC.MMCSimulation();
        XYLineChart_AWT chart = new XYLineChart_AWT("The simulation of system",
                "M/M/C"
                , simulationMMC.getX_axis(), simulationMMC.getY_axis());
        chart.pack();
        RefineryUtilities.centerFrameOnScreen(chart);
        chart.setVisible(true);
    }

    public static class MMCSimulation {
        private static int num_in_system = 0;  //Y-axis
        private static int num_arrival = 0;
        private static int num_departure = 0;
        private static double total_waite = 0;
        private static double wait_in_system = 0;

        static Queue<Double> queue = new LinkedList<>();       // arrival times of customers
        private static double nextArrival;     // time of next arrival
        private static double nextDeparture;  // time of next departure
        static double clock = 0;//X-axis
        private static ArrayList<Double> x_axis = new ArrayList<>();
        private static ArrayList<Double> y_axis = new ArrayList<>();
        private static PriorityQueue<Double> priorityQueue = new PriorityQueue<>();

        private void process() {
            Queue<Double> queue = new LinkedList<>();       // arrival times of customers
            //nextArrival   = StdRandom.exp(1/lamba);     // time of next arrival
            nextArrival = 0;     // time of next arrival
            nextDeparture = Double.POSITIVE_INFINITY;  // time of next departure


         //   System.out.println("Serial, Clock, Event,#Arrival, #Depature,#InSystem,Wait");
            // simulate an M/M/1 queue
            for (int i = 1; i <= num_simulation; i++) {

                // it's an arrival
                if (priorityQueue.isEmpty() || nextArrival <= priorityQueue.peek()) {
                    clock = nextArrival;
                    handel_arrival_event();

                //    System.out.println(i + "," + clock + "," + "Arrival" + "," + num_arrival + "," + num_departure + "," + num_in_system + ",none");
                }
                // it's a departure
                else {
                    clock = priorityQueue.peek();
                    handel_depture_event();
                //    System.out.println(i + "," + clock + "," + "Depture" + "," + num_arrival + "," + num_departure + "," + num_in_system + "," + wait_in_system);
                }
                x_axis.add((clock));
                y_axis.add((double) num_in_system);
            }

        }

        private static void handel_arrival_event() {
            num_arrival++;
            num_in_system++;
            if (priorityQueue.size() < c) {
                double service_time = exp(mu);
                double temp = nextArrival + service_time;
                priorityQueue.add(temp);
            } else {
                queue.add(nextArrival);
            }


            double interarrival_time = exp(lamda);
            nextArrival = nextArrival + interarrival_time;

        }

        private static void handel_depture_event() {
            num_departure++;

            num_in_system--;

            wait_in_system = nextDeparture - priorityQueue.poll();
            total_waite += wait_in_system;
            if (priorityQueue.isEmpty())
                nextDeparture = Double.POSITIVE_INFINITY;
            else {
                double service_time = exp(mu);
                if (!queue.isEmpty()) {
                    nextDeparture = queue.poll() + service_time;
                    priorityQueue.add(nextDeparture);
                }
            }
        }

        public ArrayList<Double> getX_axis() {
            return x_axis;
        }

        public ArrayList<Double> getY_axis() {
            return y_axis;
        }

        private static double exp(double var) {
            Random rand = new Random();
            return Math.log(1 - rand.nextDouble()) / (-var);
        }
    }

}
