package cosc320;

public class Benchmarker {
    private long start = -1;
    private long end = -1;
    private boolean running;
    public void Start(){
        start = System.nanoTime();
        running = true;
    }
    public void Stop(){
        end = System.nanoTime();
        running = false;
    }
    public long Elapsed(){
        if(!running){
            return (end - start)/1000000;
        }
        return (System.nanoTime() - start)/1000000;
    }
    public long ElapsedNano(){
        if(!running){
            return (end - start);
        }
        return (System.nanoTime() - start);
    }

    public void PrintRuntimeOfThisCode(String msg, Runnable fn){
        Start();
        fn.run();
        Stop();
        System.out.println(msg + Elapsed() + "ms");
    }
}
