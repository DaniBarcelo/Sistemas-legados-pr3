package practica3.domain;

public class Game {
    

    private String id;
    private String name;
    private String type;
    private String location;
    private String register;


    public Game(){
        this.id=null;
        this.name=null;
        this.type=null;
        this.location=null;
        this.register=null;
    }

    public Game(String id, String name, String type, String location, String register){
        this.id=id;
        this.name=name;
        this.type=type;
        this.location=location;
        this.register=register;
    }

    public Game(String id, String name, String type, String location){
        this.id=id;
        this.name=name;
        this.type=type;
        this.location=location;
        this.register=null;
    }

    public String getId() { return this.id; }
    public String getName() { return this.name; }
    public String getType() { return this.type; }
    public String getLocation(){
        return this.location;
    }
    public String getRegister(){
        return this.register;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Game game = (Game) o;

        if (!id.equals(game.id)) return false;
        if (!name.equals(game.name)) return false;
        if (type != null ? !type.equals(game.type) : game.type != null) return false;
        if (location != null ? !location.equals(game.location) : game.location != null) return false;
        return register != null ? register.equals(game.register) : game.register == null;
    }





}
