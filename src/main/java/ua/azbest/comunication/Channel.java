package ua.azbest.comunication;

import ua.azbest.model.Model;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;

public class Channel implements Runnable {

    private BlockingQueue<Message> queue;
    private Model model;
    private final long DELAY = 100;
    private final Random random = new Random();

    public Channel(Model model) {
        this.model = model;
        queue = new DelayQueue<>();
    }

    public void push(Message message) {
        try {
            queue.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        while (model.isActive()) {
            if (!queue.isEmpty()) {
                try {
                    Message m = queue.take();
                    model.getMachines().get((int) (random.nextInt(model.getMachines().size()))).receiveMessage(m);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Channel CLOSE");
    }
}