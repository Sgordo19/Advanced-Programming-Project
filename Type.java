package Project;

public enum Type {
    STANDARD(1.0, "Standard Delivery"),
    EXPRESS(2.0, "Express Delivery"),
    FRAGILE(3.0, "Fragile Handling");
    
    private final double surchargeMultiplier;
    private final String description;
    
    Type(double surchargeMultiplier, String description) {
        this.surchargeMultiplier = surchargeMultiplier;
        this.description = description;
    }
    
    public double getSurchargeMultiplier() {
        return surchargeMultiplier;
    }
    
    public String getDescription() {
        return description;
    }
    
    public static Type fromDescription(String description) {
        for (Type type : values()) {
            if (type.getDescription().equals(description)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown type description: " + description);
    }
    
    public static Type fromName(String name) {
        try {
            return Type.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown type name: " + name);
        }
    }
    
    @Override
    public String toString() {
        return description;
    }
}