package com.interiordesigner.Interfaces;

import com.interiordesigner.Classes.CardType;

public interface Card {
    CardType getType();
    int getId();
    int getLevel();
    String getText();
}

