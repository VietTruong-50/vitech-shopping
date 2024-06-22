package vn.hust.api.service.helper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CollaborativeFiltering {
    private Map<String, Map<String, Double>> userRatings;
    private Map<String, Map<String, Double>> itemRatings;

    public CollaborativeFiltering(Map<String, Map<String, Double>> userRatings, Map<String, Map<String, Double>> itemRatings) {
        this.userRatings = userRatings;
        this.itemRatings = itemRatings;
    }

    /**
     * Calculates the average rating of a user.
     *
     * @param ratings The map of item ratings for a user.
     * @return The average rating.
     */
    private double calculateAverageRating(Map<String, Double> ratings) {
        return ratings.values().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    /**
     * Calculates the similarity between two users based on their ratings.
     *
     * @param user1 The first user.
     * @param user2 The second user.
     * @return The similarity score between user1 and user2.
     */
    public double getUserSimilarity(String user1, String user2) {
        // Get the set of items both users have rated
        Set<String> commonItems = new HashSet<>(userRatings.get(user1).keySet());
        commonItems.retainAll(userRatings.get(user2).keySet());

        // If no common items, similarity is zero
        if (commonItems.isEmpty()) {
            return 0.0;
        }

        // Calculate the average ratings for both users
        double avgUser1 = calculateAverageRating(userRatings.get(user1));
        double avgUser2 = calculateAverageRating(userRatings.get(user2));

        // Variables to store the sum of squared differences and sums of squares
        double sumSqrt1 = 0.0;
        double sumSqrt2 = 0.0;
        double sumSquaredDiff = 0.0;

        // Calculate the sum of squared differences and sums of squares
        for (String item : commonItems) {
            double rating1 = userRatings.get(user1).get(item);
            double rating2 = userRatings.get(user2).get(item);
            double v1 = rating1 - avgUser1;
            double v2 = rating2 - avgUser2;

            sumSquaredDiff += v1 * v2;
            sumSqrt1 += Math.pow(v1, 2);
            sumSqrt2 += Math.pow(v2, 2);
        }

        // Avoid division by zero
        if (sumSqrt1 == 0 || sumSqrt2 == 0) {
            return 0.0;
        }

        // Return the cosine similarity
        return sumSquaredDiff / (Math.sqrt(sumSqrt1) * Math.sqrt(sumSqrt2));
    }

    /**
     * Generates recommendations for a target user based on the ratings of similar users.
     *
     * @param targetUser The user for whom recommendations are generated.
     * @return A map of item recommendations with their scores.
     */
    public Map<String, Double> getUserRecommendations(String targetUser) {
        Map<String, Double> recommendations = new HashMap<>();

        // Iterate over all users except the target user
        for (String user : userRatings.keySet()) {
            if (!user.equals(targetUser)) {
                // Calculate similarity between target user and current user
                double similarity = getUserSimilarity(targetUser, user);

                // If similarity is positive, proceed to generate recommendations
                if (similarity > 0) {
                    for (String item : userRatings.get(user).keySet()) {
                        // Recommend items the target user hasn't rated yet
//                        if (!userRatings.get(targetUser).containsKey(item) || userRatings.get(targetUser).get(item) == 0.0) {
                            // Merge the item into the recommendations map with weighted scores
                            recommendations.merge(item, userRatings.get(user).get(item) * similarity, Double::sum);
//                        }
                    }
                }
            }
        }

        return recommendations;
    }

    /**
     * Calculates the similarity between two items based on their ratings by common users.
     * @param item1 The first item.
     * @param item2 The second item.
     * @return The similarity score between item1 and item2.
     */
    private double getItemSimilarity(String item1, String item2) {
        // Get the set of users who have rated both items
        Set<String> commonUsers = new HashSet<>(itemRatings.get(item1).keySet());
        commonUsers.retainAll(itemRatings.get(item2).keySet());

        // If no common users, similarity is zero
        if (commonUsers.isEmpty()) {
            return 0.0;
        }

        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        // Calculate the dot product and norms for the two items
        for (String user : commonUsers) {
            double rating1 = itemRatings.get(item1).get(user);
            double rating2 = itemRatings.get(item2).get(user);

            dotProduct += rating1 * rating2;
            norm1 += Math.pow(rating1, 2);
            norm2 += Math.pow(rating2, 2);
        }

        // Avoid division by zero
        if (norm1 == 0 || norm2 == 0) {
            return 0.0;
        }

        // Return the cosine similarity
        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    /**
     * Generates recommendations for a target item based on the ratings of similar items.
     * @param targetItem The item for which recommendations are generated.
     * @return A map of user recommendations with their scores.
     */
    public Map<String, Double> getItemRecommendations(String targetItem) {
        Map<String, Double> recommendations = new HashMap<>();

        // Iterate over all items except the target item
        for (String item : itemRatings.keySet()) {
            if (!item.equals(targetItem)) {
                // Calculate similarity between target item and current item
                double similarity = getItemSimilarity(targetItem, item);

                // If similarity is positive, proceed to generate recommendations
                if (similarity > 0) {
                    for (String user : itemRatings.get(item).keySet()) {
                        // Recommend to users who haven't rated the target item yet
                        if (!itemRatings.get(targetItem).containsKey(user) || itemRatings.get(targetItem).get(user) == 0.0) {
                            // Merge the user into the recommendations map with weighted scores
                            recommendations.merge(user, itemRatings.get(item).get(user) * similarity, Double::sum);
                        }
                    }
                }
            }
        }

        return recommendations;
    }
}
