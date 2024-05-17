package vn.vnpt.api.service.helper;

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

    private double calculateAverageRating(Map<String, Double> ratings) {
        return ratings.values().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    public double getUserSimilarity(String user1, String user2) {
        Set<String> commonItems = new HashSet<>(userRatings.get(user1).keySet());
        commonItems.retainAll(userRatings.get(user2).keySet());

        if (commonItems.isEmpty()) {
            return 0.0; // No common items, similarity is zero
        }

        double avgUser1 = calculateAverageRating(userRatings.get(user1));
        double avgUser2 = calculateAverageRating(userRatings.get(user2));

        double sumSqrt1 = 0.0;
        double sumSqrt2 = 0.0;
        double sumSquaredDiff = 0.0;

        for (String item : commonItems) {
            double rating1 = userRatings.get(user1).get(item);
            double rating2 = userRatings.get(user2).get(item);
            double v1 = rating1 - avgUser1;
            double v2 = rating2 - avgUser2;

            sumSquaredDiff += v1 * v2;
            sumSqrt1 += Math.pow(v1, 2);
            sumSqrt2 += Math.pow(v2, 2);
        }

        if (sumSqrt1 == 0 || sumSqrt2 == 0) {
            return 0.0; // Avoid division by zero
        }

        return sumSquaredDiff / (Math.sqrt(sumSqrt1) * Math.sqrt(sumSqrt2));
    }

    public Map<String, Double> getUserRecommendations(String targetUser) {
        Map<String, Double> recommendations = new HashMap<>();

        for (String user : userRatings.keySet()) {
            if (!user.equals(targetUser)) {
                double similarity = getUserSimilarity(targetUser, user);

                if (similarity > 0) {
                    for (String item : userRatings.get(user).keySet()) {
                        if (!userRatings.get(targetUser).containsKey(item) || userRatings.get(targetUser).get(item) == 0.0) {
                            recommendations.merge(item, userRatings.get(user).get(item) * similarity, Double::sum);
                        }
                    }
                }
            }
        }

        return recommendations;
    }

    private double getItemSimilarity(String item1, String item2) {
        Set<String> commonUsers = new HashSet<>(itemRatings.get(item1).keySet());
        commonUsers.retainAll(itemRatings.get(item2).keySet());

        if (commonUsers.isEmpty()) {
            return 0.0; // No common users, similarity is zero
        }

        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (String user : commonUsers) {
            double rating1 = itemRatings.get(item1).get(user);
            double rating2 = itemRatings.get(item2).get(user);

            dotProduct += rating1 * rating2;
            norm1 += Math.pow(rating1, 2);
            norm2 += Math.pow(rating2, 2);
        }

        if (norm1 == 0 || norm2 == 0) {
            return 0.0; // Avoid division by zero
        }

        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    public Map<String, Double> getItemRecommendations(String targetItem) {
        Map<String, Double> recommendations = new HashMap<>();

        for (String item : itemRatings.keySet()) {
            if (!item.equals(targetItem)) {
                double similarity = getItemSimilarity(targetItem, item);

                if (similarity > 0) {
                    for (String user : itemRatings.get(item).keySet()) {
                        if (!itemRatings.get(targetItem).containsKey(user) || itemRatings.get(targetItem).get(user) == 0.0) {
                            recommendations.merge(user, itemRatings.get(item).get(user) * similarity, Double::sum);
                        }
                    }

                }
            }
        }

        return recommendations;
    }
}
