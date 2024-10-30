package models;

/**
 * Used to calculate the score of throws with 5 dice
 */
public class YatzyResultCalculator {
    private final Die[] dice;
    /**
     *
     * @param dice
     */
    public YatzyResultCalculator(Die[] dice) {
        //TODO: implement YatzyResultCalculator constructor.
        this.dice = dice;
    }

    /**
     * Calculates the score for Yatzy uppersection
     * @param eyes eye value to calculate score for. eyes should be between 1 and 6
     * @return the score for specified eye value
     */
    public int upperSectionScore(int eyes) {
        //TODO: Implement upperSectionScore method.
        int score = 0;
        for (Die die : dice) {
            if (die.getEyes() == eyes) {
                score += eyes;
            }
        }
        return score;
    }

    public int onePairScore() {
        //TODO: implement onePairScore method.
        int[] counts = new int[7];

        for (Die die : dice) {
            counts[die.getEyes()]++;
        }

        for (int i = 6; i > 0; i--) {
            if (counts[i] >= 2) {
                return i * 2;
            }
        }
        return 0;
    }

    public int twoPairScore() {
        //TODO: implement twoPairScore method.
        {
            int[] counts = new int[7];

            for (Die die : dice) {
                counts[die.getEyes()]++;
            }

            int pairsFound = 0;
            int score = 0;

            for (int i = 6; i > 0; i--) {
                if (counts[i] >= 2) {
                    pairsFound++;
                    score += i * 2;
                    counts[i] -= 2;
                    if (pairsFound == 2) {
                        return score;
                    }
                }
            }
        }
            return 0;
    }

    public int threeOfAKindScore() {
        //TODO: implement threeOfAKindScore method.
        int[] counts = new int[7];

        for (Die die : dice) {
            counts[die.getEyes()]++;
        }

        for (int i = 6; i > 0; i--) {
            if (counts[i] >= 3) {
                return i * 3;
            }
        }
        return 0;
    }

    public int fourOfAKindScore() {
        //TODO: implement fourOfAKindScore method.
        int[] counts = new int[7];

        for (Die die : dice) {
            counts[die.getEyes()]++;
        }

        for (int i = 6; i > 0; i--) {
            if (counts[i] >= 4) {
                return i * 4;
            }
        }
        return 0;
    }

    public int smallStraightScore() {
        //TODO: implement smallStraightScore method.
        int[] counts = new int[7];

        for (Die die : dice) {
            counts[die.getEyes()]++;
        }

        if (counts[1] >= 1 && counts[2] >= 1 && counts[3] >= 1 && counts[4] >= 1 && counts[5] >= 1) {
            return 15;
        }

        return 0;
    }

    public int largeStraightScore() {
        //TODO: implement largeStraightScore method.
        int[] counts = new int[7];

        for (Die die : dice) {
            counts[die.getEyes()]++;
        }

        if (counts[2] >= 1 && counts[3] >= 1 && counts[4] >= 1 && counts[5] >= 1 && counts[6] >= 1) {
            return 20;
        }

        return 0;
    }

    public int fullHouseScore() {
        //TODO: implement fullHouseScore method.
        int[] counts = new int[7];
        for (Die die : dice) {
            counts[die.getEyes()]++;
        }

        int threeOfAKindValue = 0;
        int pairValue = 0;

        for (int i = 6; i > 0; i--) {
            if (counts[i] >= 3) {
                if (threeOfAKindValue == 0) {
                    threeOfAKindValue = i;
                } else if (pairValue == 0 && counts[i] >= 2) {
                    pairValue = i;
                }
            } else if (counts[i] >= 2 && pairValue == 0) {
                pairValue = i;
            }
        }

        if (threeOfAKindValue != 0 && pairValue != 0) {
            return (threeOfAKindValue * 3) + (pairValue * 2);
        }

        return 0;
    }

    public int chanceScore() {
        //TODO: implement chanceScore method.
        int score = 0;
        for (Die die : dice) {
            score += die.getEyes();
        }
        return score;
    }

    public int yatzyScore() {
        //TODO: implement yatzyScore method.
        int[] counts = new int[7];

        for (Die die : dice) {
            counts[die.getEyes()]++;
        }

        for (int i = 6; i > 0; i--) {
            if (counts[i] >= 5) {
                return 50;
            }
        }
        return 0;
    }
}