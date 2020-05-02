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

import static java.util.stream.Collectors.toCollection;

public class Model implements Runnable {

    private final int clusterSize;
    private Channel channel;
    private final List<Machine> machines;
    private final double TASK_SIZE = 1000;
    private boolean active = false;

    public Model(int clusterSize) {
        Random random = new Random();
        this.clusterSize = clusterSize;
        ZeroMachine zeroMachine = new ZeroMachine(random.nextDouble(), this);
//        machines = Stream.generate(() -> new Machine(this)).limit(clusterSize).collect(Collectors.toList());
        machines = Stream.generate(() -> new Machine(random.nextDouble(), this)).limit(clusterSize).collect(toCollection(ArrayList::new));
        machines.add(0, zeroMachine);
        active = true;
        channel = new Channel(this);
        machines.get(0).receiveMessage(new Message(0, TASK_SIZE));
        Thread th = new Thread(channel);
        try {
            th.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        th.start();
    }

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

    public void printInfo() {
        machines.forEach(System.out::println);
    }

    public List<Machine> getMachines() {
        return machines;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        while (active) {
            active = getPassiveMachineCount() != clusterSize;
            System.out.println("\t\t\t\t Left: " + getTaskLeft());

            //machines.stream().filter(m -> m.getCurrentState() instanceof ActiveMachine).mapToInt(Machine::getId).forEach(System.out::println);

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(
                "Model STOP!"
        );
        long diff = System.currentTimeMillis() - startTime;
        System.out.println("Whole time calculated: " + diff);
        machines.stream().map(Machine::getStatisticInfo).forEach(System.out::println);
        machines.stream().mapToDouble((m -> m.getWorkingTime())).map(d -> d/diff*100).forEach(System.out::println);
    }

    public int getSumBaseCounter() {
        return machines.stream().mapToInt(m -> m.getBaseCounter()).sum();
    }

    public boolean isActive() {
        return active;
    }
}
