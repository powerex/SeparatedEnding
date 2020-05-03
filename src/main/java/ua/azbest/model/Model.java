package ua.azbest.model;

import ua.azbest.comunication.Channel;
import ua.azbest.comunication.Message;
import ua.azbest.machine.ActiveMachine;
import ua.azbest.machine.Machine;
import ua.azbest.machine.ZeroMachine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static java.lang.Thread.*;
import static java.util.stream.Collectors.toCollection;

public class Model implements Runnable {

    private final int clusterSize;
    private final Channel channel;
    private final List<Machine> machines;
    private final double TASK_SIZE = 200;
    private boolean active = false;

    private Thread pr;

    public Model(int clusterSize) {
        Random random = new Random();
        this.clusterSize = clusterSize;
        ZeroMachine zeroMachine = new ZeroMachine(random.nextDouble(), this);
        machines = Stream.generate(() -> new Machine(random.nextDouble(), this)).limit(clusterSize-1).collect(toCollection(ArrayList::new));
        machines.add(0, zeroMachine);

        active = true;
        channel = new Channel(this);
        machines.get(0).receiveMessage(new Message(0, TASK_SIZE));
        Probe probe = new Probe(this);
        Thread ch = new Thread(this.channel);
        pr = new Thread(probe);
        try {
            ch.join();
            pr.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ch.start();
        getZeroMachine().sendToken();
        pr.start();
    }

    /**
     * @return double value - sum of all taskSize form ActiveMachines
     *         value not includes task in delayed messages in channel
     */
    public double getTaskLeft() {
        return machines.stream().filter(m -> m.getCurrentState() instanceof ActiveMachine).mapToDouble(Machine::getTaskSize).sum();
    }

    public long getActiveMachineCount() {
        return machines.stream().filter(m -> m.getCurrentState() instanceof ActiveMachine).count();
    }

    public long getPassiveMachineCount() {
        return clusterSize - getActiveMachineCount();
    }

    public Channel getChannel() {
        return channel;
    }

    public List<Machine> getMachines() {
        return machines;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        while (active) {
            System.out.println("\t\t\t\t Left: " + getTaskLeft());

            try {
                sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        System.out.println("\n\n Machine base Counter ");
        System.out.println("Sum base counters " + machines.stream().mapToInt(Machine::getBaseCounter).sum());
        machines.stream().mapToInt(Machine::getBaseCounter).forEach(System.out::println);
        System.out.println("End Machine base Counter ");

        long diff = System.currentTimeMillis() - startTime;
        System.out.println("Whole time calculated: " + diff);
        machines.stream().map(Machine::getStatisticInfo).forEach(System.out::println);
        machines.stream().mapToDouble((Machine::getWorkingTime)).map(d -> d/diff*100).forEach(System.out::println);

        getZeroMachine().sendToken();
    }

    public boolean isActive() {
        return active;
    }

    public ZeroMachine getZeroMachine() {
        Machine zero = machines.get(0);
        if (zero instanceof ZeroMachine)
            return (ZeroMachine)zero;
        return null;
    }

    public int sumBaseCounters() {
        return machines.stream().mapToInt(Machine::getBaseCounter).sum();
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
