package test;

import javax.persistence.Persistence;

public class Builder
{
    public static void main(String[] args)
    {
         Persistence.generateSchema("SEED_PU", null);
    }
}
