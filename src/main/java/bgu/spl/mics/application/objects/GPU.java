package bgu.spl.mics.application.objects;

/**
 * Passive object representing a single GPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class GPU {
    /**
     * Enum representing the type of the GPU.
     */
    enum Type {RTX3090, RTX2080, GTX1080}

    private Type type;
    private Cluster cluster;
    private Model model;

    public GPU (Type type, Cluster cluster){
        this.type = type;
        this.cluster = cluster;
        this.model = null;
    }

    public Model trainModel (Model model){
        return model;
    }

    public Model getModel(){
        return model;
    }

    public Type getType(){
        return type;
    }

}
