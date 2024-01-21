import org.jfree.ui.RefineryUtilities;

import java.lang.Math;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class MM1K {

    private static double lamda;
    private static double mu;
    private static int k;
    private static int num_simulation;

    public MM1K(double lamda, double mu, int k, int num_simulation) {
        MM1K.lamda = lamda;
        MM1K.mu = mu;
        MM1K.k = k;
        MM1K.num_simulation = num_simulation;
    }

    private double Pn(int n) {
        double ru = lamda / mu;
        if (ru != 1) {
            return (Math.pow(ru, n)) * ((1 - ru) / (1 - Math.pow(ru, k + 1)));
        }
        return 1.0 / (k + 1);
    }

    private double customerInSystem() {
        double ru = lamda / mu;
        if (ru != 1) {
            double numerator = (1 - (k + 1) * Math.pow(ru, k) + k * Math.pow(ru, k + 1)); // البسط
            double denominator = (1 - ru) * (1 - Math.pow(ru, k + 1));                      // المقام
            return ru * (numerator / denominator);
        }
        return k / 2.0;
    }

    private double customerInQueue() {
        double ru = lamda / mu;
        return this.customerInSystem() - ru * (1 - this.Pn(k));

    }

    private double timeInSystem() {


        return this.customerInSystem() / (lamda * (1 - this.Pn(k)));
    }

    private double timeInQueue() {
        return this.timeInSystem() - (1 / mu);
    }

    public void displayInformation() {
        System.out.printf("%s%.4f\n", "L = ", customerInSystem());
        System.out.printf("%s%.4f\n", "Lq = ", customerInQueue());
        System.out.printf("%s%.4f\n", "W = ", timeInSystem());
        System.out.printf("%s%.4f\n", "Wq = ", timeInQueue());
        MM1K.SimulationMM1K simulationMM1K= new SimulationMM1K();
        simulationMM1K.process();
        drawingSystem();
    }

    private void drawingSystem() {
        MM1K.SimulationMM1K simulationMM1K = new SimulationMM1K();
        XYLineChart_AWT chart = new XYLineChart_AWT("The simulation of system",
                "M/M/1/K"
                , simulationMM1K.getX_axis(), simulationMM1K.getY_axis());
        chart.pack();
        RefineryUtilities.centerFrameOnScreen(chart);
        chart.setVisible(true);
    }

    static class SimulationMM1K {
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

        private void process() {
            // arrival times of customers
            //nextArrival   = StdRandom.exp(1/lamba);     // time of next arrival
            nextArrival = 0;     // time of next arrival
            nextDeparture = Double.POSITIVE_INFINITY;  // time of next departure


            System.out.println("Serial, Clock, Event,#Arrival, #Depature,#InSystem,Wait");
            // simulate an M/M/1 queue
            for (int i = 1; i <= num_simulation; i++) {

                // it's an arrival
                if (nextArrival <= nextDeparture) {
                    if (k==queue.size()){
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
                    clock = nextDeparture;
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
            if (queue.isEmpty()) {
                double service_time = exp(mu);
                nextDeparture = nextArrival + service_time;
            }
            queue.add(nextArrival);
            double interarrival_time = exp(lamda);
            nextArrival = nextArrival + interarrival_time;
        }

        private static void handel_depture_event() {
            num_departure++;
            num_in_system--;
            wait_in_system = nextDeparture - queue.poll();
            total_waite += wait_in_system;
            if (queue.isEmpty())
                nextDeparture = Double.POSITIVE_INFINITY;
            else {
                double service_time = exp(mu);
                nextDeparture = nextDeparture + service_time;
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

