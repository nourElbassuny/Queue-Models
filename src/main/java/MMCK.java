import org.jfree.ui.RefineryUtilities;

import java.util.*;

public class MMCK {
    private static double lamda;
    private static double mu;
    private static int c;
    private static int k;
    private static int num_simulation;
    private static double r;  // lamda/mu
    private static double ru; //r/c

    public MMCK(double lamda, double mu, int c, int k, int num_simulation) {
        MMCK.lamda = (double) lamda;
        MMCK.mu = (double) mu;
        MMCK.c = c;
        MMCK.k = k;
        MMCK.num_simulation = num_simulation;
        r = MMCK.lamda / MMCK.mu;
        ru = r / MMCK.c;
    }

    private int fact(int n) {
        int res = 1;
        for (int i = 2; i <= n; i++) {
            res *= i;
        }
        return res;
    }

    private double P0() {
        // case one
        if (ru != 1) {
            double part1 = 0;
            for (int n = 0; n < c; n++) {
                part1 += (Math.pow(r, n) / this.fact(n));
            }
            double part2 = (Math.pow(r, c) / this.fact(c)) * ((1 - Math.pow(ru, k - c + 1)) / (1 - ru));
            return 1 / (part1 + part2);
        }
        // case two
        double part1 = 0;
        for (int n = 0; n < c; n++) {
            part1 += (Math.pow(r, n) / this.fact(n));
        }
        double part2 = (Math.pow(r, c) / this.fact(c)) * (k - c + 1);
        return 1 / (part1 + part2);
    }

    private double Pn(int n) {
        if (n >= 0 && n < c) {
            return (Math.pow(r, n) / this.fact(n)) * this.P0();
        }
        return ((Math.pow(r, n)) / (Math.pow(c, n - c) * this.fact(c))) * this.P0();
    }

    private double customerInQueue() {
        double part1 = (ru * Math.pow(r, c) * this.P0()) / (this.fact(c) * Math.pow((1 - ru), 2));
        double part2 = 1 - Math.pow(ru, k - c + 1) - (1 - ru) * (k - c + 1) * Math.pow(ru, k - c);
        return part1 * part2;
    }

    private double customerInSystem() {
        double sum = 0;
        for (int n = 0; n <= c - 1; n++) {
            sum += ((c - n) * (Math.pow(r, n) / this.fact(n)));
        }
        return this.customerInQueue() + c - this.P0() * sum;
    }

    private double timeInSystem() {
        return this.customerInSystem() / (lamda * (1 - this.Pn(k)));
    }

    private double timeInQueue() {
        return this.customerInQueue() / (lamda * (1 - this.Pn(k)));
    }

    public void displayInformation() {
        System.out.printf("%s%.4f\n", "L = ", customerInSystem());
        System.out.printf("%s%.4f\n", "Lq = ", customerInQueue());
        System.out.printf("%s%.4f\n", "W = ", timeInSystem());
        System.out.printf("%s%.4f\n", "Wq = ", timeInQueue());
        MMCK.SimulationMMCK simulation = new MMCK.SimulationMMCK();
        simulation.process();
        drawingSystem();
    }

    private void drawingSystem() {
        MMCK.SimulationMMCK simulationMMCK = new SimulationMMCK();
        XYLineChart_AWT chart = new XYLineChart_AWT("The simulation of system",
                "M/M/C/k"
                , simulationMMCK.getX_axis(), simulationMMCK.getY_axis());
        chart.pack();
        RefineryUtilities.centerFrameOnScreen(chart);
        chart.setVisible(true);
    }
    static class SimulationMMCK {
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
             // arrival times of customers
            //nextArrival   = StdRandom.exp(1/lamba);     // time of next arrival
            nextArrival = 0;     // time of next arrival
            nextDeparture = Double.POSITIVE_INFINITY;  // time of next departure


            System.out.println("Serial, Clock, Event,#Arrival, #Depature,#InSystem,Wait");
            // simulate an M/M/1 queue
            for (int i = 1; i <= num_simulation; i++) {

                // it's an arrival
                if (priorityQueue.isEmpty() || nextArrival <= priorityQueue.peek()) {
                    if (queue.size()+priorityQueue.size()>=k){
                        double interarrival_time = exp(lamda);
                        nextArrival = nextArrival + interarrival_time;
                        continue;
                    }
                    clock = nextArrival;
                    handel_arrival_event();

                    System.out.println(i + "," + clock + "," + "Arrival" + "," + num_arrival + "," + num_departure + "," + num_in_system + ",none");
                }
                // it's a departure
                else {
                    clock = priorityQueue.peek();
                    handel_depture_event();
                    System.out.println(i + "," + clock + "," + "Depture" + "," + num_arrival + "," + num_departure + "," + num_in_system + "," + wait_in_system);
                }
                x_axis.add(clock);
                y_axis.add((double)num_in_system);
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

