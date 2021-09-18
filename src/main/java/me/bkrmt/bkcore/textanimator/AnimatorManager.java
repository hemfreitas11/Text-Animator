package me.bkrmt.bkcore.textanimator;

import me.bkrmt.bkcore.textanimator.animations.RGBRainbowAnimation;
import me.bkrmt.bkcore.textanimator.animations.ShineAnimation;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.bukkit.ChatColor.COLOR_CHAR;

public class AnimatorManager {
    private final ConcurrentHashMap<String, TextAnimator> animators;
    private final JavaPlugin plugin;

    public AnimatorManager(JavaPlugin plugin) {
        this.plugin = plugin;
        animators = new ConcurrentHashMap<>();
//        debug();
    }

    public ConcurrentHashMap<String, TextAnimator> getAnimators() {
        return animators;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public TextAnimator createTextAnimator(String animatorName, String initialText) {
        TextAnimator newAnimator = new TextAnimator(plugin, animatorName, initialText);
        if (animators.size() > 0) {
            TextAnimator tempAnimator = animators.get(animatorName);
            if (tempAnimator != null) {
                if (tempAnimator.getAnimationTask() != null) {
                    tempAnimator.destroy();
                }
                animators.remove(tempAnimator.getIdentifier());
            }
        }
        animators.put(animatorName, newAnimator);
        return newAnimator;
    }

    public static AnimationPlaceholder deserializePlaceholder(String text) {
        AnimationPlaceholder returnValue = null;

        if (isAnimation(text)) {
            text = text.trim();
            String animationString = "";

            Pattern pattern = Pattern.compile("\\{([^}]*)}");
            Matcher matcher = pattern.matcher(text);
            if (matcher.find()) {
                animationString = matcher.group(1);
            }
            int size = -1;
            int pauseAmount = -1;
            int updateFrequency = -1;
            String optionText = "";

            if (animationString.contains("size")) {
                Pattern sizePattern = Pattern.compile("size\\[([^]]*)]");
                Matcher sizeMatcher = sizePattern.matcher(animationString);
                if (sizeMatcher.find()) {
                    try {
                        size = Integer.parseInt(sizeMatcher.group(1));
                    } catch (Exception ignored) {
                    }
                }
            }

            if (animationString.contains("pause")) {
                Pattern pausePattern = Pattern.compile("pause\\[([^]]*)]");
                Matcher pauseMatcher = pausePattern.matcher(animationString);
                if (pauseMatcher.find()) {
                    try {
                        pauseAmount = Integer.parseInt(pauseMatcher.group(1));
                    } catch (Exception ignored) {
                    }
                }
            }

            if (animationString.contains("speed")) {
                Pattern updatePattern = Pattern.compile("speed\\[([^]]*)]");
                Matcher updateMatcher = updatePattern.matcher(animationString);
                if (updateMatcher.find()) {
                    try {
                        updateFrequency = Integer.parseInt(updateMatcher.group(1));
                    } catch (Exception ignored) {
                    }
                }
            }

            if (animationString.contains("text")) {
                Pattern textPattern = Pattern.compile("text\\[([^]]*)]");
                Matcher textMatcher = textPattern.matcher(animationString);
                if (textMatcher.find()) {
                    optionText = textMatcher.group(1);
                    if (!optionText.isEmpty()) optionText = ChatColor.stripColor(textMatcher.group(1));
                }
            }

            String color = COLOR_CHAR + "6";
            if (animationString.contains("yellow")) {
                color = COLOR_CHAR + "e";
            } else if (animationString.contains("darkblue")) {
                color = COLOR_CHAR + "1";
            } else if (animationString.contains("darkgray")) {
                color = COLOR_CHAR + "8";
            } else if (animationString.contains("lightred")) {
                color = COLOR_CHAR + "c";
            } else if (animationString.contains("lightblue")) {
                color = COLOR_CHAR + "b";
            } else if (animationString.contains("lightgreen")) {
                color = COLOR_CHAR + "a";
            } else if (animationString.contains("blue")) {
                color = COLOR_CHAR + "9";
            } else if (animationString.contains("green")) {
                color = COLOR_CHAR + "2";
            } else if (animationString.contains("red")) {
                color = COLOR_CHAR + "4";
            } else if (animationString.contains("purple")) {
                color = COLOR_CHAR + "5";
            } else if (animationString.contains("white")) {
                color = COLOR_CHAR + "f";
            } else if (animationString.contains("black")) {
                color = COLOR_CHAR + "0";
            } else if (animationString.contains("gray")) {
                color = COLOR_CHAR + "7";
            } else if (animationString.contains("pink")) {
                color = COLOR_CHAR + "d";
            }

            if (animationString.contains(AnimationType.SHINE.getString())) {
                returnValue = new AnimationPlaceholder(text, color, animationString.contains("bold"), AnimationType.SHINE,
                        new AnimationOptions(optionText, updateFrequency, pauseAmount, size)
                );
            } else if (animationString.contains(AnimationType.RGB_RAINBOW.getString())) {
                returnValue = new AnimationPlaceholder(text, color, animationString.contains("bold"), AnimationType.RGB_RAINBOW,
                        new AnimationOptions(optionText, updateFrequency, pauseAmount, size)
                );
            }
            return returnValue;
        }
        return null;
    }

    public TextAnimator getTextAnimator(String identifier, String text) {
        TextAnimator animator = null;

        if (isAnimation(text)) {
            AnimationPlaceholder placeholder = deserializePlaceholder(text);
            if (placeholder != null) {

                boolean bold = placeholder.isBold();
                String color = (placeholder.getColor() != null ? placeholder.getColor() : COLOR_CHAR + "6") + (placeholder.isBold() ? COLOR_CHAR + "l" : "");
                TextAnimation animation = null;

                if (placeholder.getAnimationType().equals(AnimationType.SHINE))
                    animation = new ShineAnimation(color, bold, color + (placeholder.getOptions().getText().isEmpty() ? placeholder.getCleanText() : placeholder.getOptions().getText()));
                else if (placeholder.getAnimationType().equals(AnimationType.RGB_RAINBOW))
                    animation = new RGBRainbowAnimation(bold, color + (placeholder.getOptions().getText().isEmpty() ? placeholder.getCleanText() : placeholder.getOptions().getText()));

                if (animation != null) {
                    animator = createTextAnimator(identifier, (placeholder.getOptions().getText().isEmpty() ? placeholder.getCleanText() : placeholder.getOptions().getText()));
                    animator
                            .setAnimation(animation)
                            .setSize(placeholder.getOptions().getSize() < 0 ? 4 : placeholder.getOptions().getSize())
                            .setPauseAmount(placeholder.getOptions().getPause() < 0 ? 5 : placeholder.getOptions().getPause())
                            .setUpdateFrequency(placeholder.getOptions().getSpeed() < 0 ? 2 : placeholder.getOptions().getSpeed());
                }
            }

        }

        /*if (text.contains("{") && text.contains("}")) {
            String animationString = "";
            Pattern pattern = Pattern.compile("\\{([^}]*)}");
            Matcher matcher = pattern.matcher(text);
            if (matcher.find())
                animationString = matcher.group(1);

            if (!animationString.isEmpty()) {
                int size = 4;
                int pauseAmount = 3;
                int updateFrequency = 2;
                String optionText = "";
                if (animationString.contains("size")) {
                    Pattern sizePattern = Pattern.compile("size\\[([^]]*)]");
                    Matcher sizeMatcher = sizePattern.matcher(animationString);
                    if (sizeMatcher.find())
                        try {
                            size = Integer.parseInt(sizeMatcher.group(1));
                        } catch (Exception exception) {
                        }
                }
                if (animationString.contains("pause")) {
                    Pattern pausePattern = Pattern.compile("pause\\[([^]]*)]");
                    Matcher pauseMatcher = pausePattern.matcher(animationString);
                    if (pauseMatcher.find())
                        try {
                            pauseAmount = Integer.parseInt(pauseMatcher.group(1));
                        } catch (Exception exception) {
                        }
                }
                if (animationString.contains("speed")) {
                    Pattern updatePattern = Pattern.compile("speed\\[([^]]*)]");
                    Matcher updateMatcher = updatePattern.matcher(animationString);
                    if (updateMatcher.find())
                        try {
                            updateFrequency = Integer.parseInt(updateMatcher.group(1));
                        } catch (Exception exception) {
                        }
                }
                if (animationString.contains("text")) {
                    Pattern textPattern = Pattern.compile("text\\[([^]]*)]");
                    Matcher textMatcher = textPattern.matcher(animationString);
                    if (textMatcher.find())
                        optionText = textMatcher.group(1);
                }

                String color = COLOR_CHAR + "6";
                if (animationString.contains("yellow")) {
                    color = COLOR_CHAR + "e";
                } else if (animationString.contains("blue")) {
                    color = COLOR_CHAR + "9";
                } else if (animationString.contains("green")) {
                    color = COLOR_CHAR + "2";
                } else if (animationString.contains("red")) {
                    color = COLOR_CHAR + "4";
                } else if (animationString.contains("purple")) {
                    color = COLOR_CHAR + "5";
                } else if (animationString.contains("white")) {
                    color = COLOR_CHAR + "f";
                } else if (animationString.contains("black")) {
                    color = COLOR_CHAR + "0";
                } else if (animationString.contains("gray")) {
                    color = COLOR_CHAR + "7";
                } else if (animationString.contains("darkblue")) {
                    color = COLOR_CHAR + "1";
                } else if (animationString.contains("darkgray")) {
                    color = COLOR_CHAR + "8";
                } else if (animationString.contains("lightred")) {
                    color = COLOR_CHAR + "c";
                } else if (animationString.contains("lightblue")) {
                    color = COLOR_CHAR + "b";
                } else if (animationString.contains("lightgreen")) {
                    color = COLOR_CHAR + "a";
                } else if (animationString.contains("pink")) {
                    color = COLOR_CHAR + "d";
                }

                boolean bold = animationString.contains("bold");
                color = bold ? (color + COLOR_CHAR + "l") : color;
                text = ChatColor.stripColor(text).replaceAll("\\{([^}]*)}", "");
                TextAnimation animation = null;

                if (optionText.isEmpty())
                    text = text.trim();

                if (animationString.contains(AnimationType.SHINE.getString()))
                    animation = new ShineAnimation(color, bold, color + (optionText.isEmpty() ? text : optionText));
                if (animation != null) {
                    animator = createTextAnimator(identifier, optionText.isEmpty() ? text : ChatColor.stripColor(optionText));
                    animator
                            .setAnimation(animation)
                            .setSize(size)
                            .setPauseAmount(pauseAmount)
                            .setUpdateFrequency(updateFrequency);
                }
            }
        }*/
        return animator;
    }


    public static String cleanText(String text) {
        text = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', text));
        String returnValue = text.replaceAll("\\{([^}]*)}", "");
        if (!text.isEmpty()) {
            Pattern pattern = Pattern.compile("\\{([^}]*)}");
            Matcher matcher = pattern.matcher(text);
            if (matcher.find()) {
                String comparator = matcher.group(1);
                if (comparator != null && !comparator.isEmpty()) {
                    Pattern textPattern = Pattern.compile("text\\[([^]]*)]");
                    Matcher textMatcher = textPattern.matcher(comparator);
                    if (textMatcher.find()) {
                        String optionText = textMatcher.group(1);
                        if (optionText != null && !optionText.isEmpty()) {
                            returnValue = text.replaceAll("\\{([^}]*)}", optionText);
                        }
                    }
                }
            }
        }
        return returnValue;
    }


    public static boolean isOptionText(String string) {
        String result = "";
        if (string != null && !string.isEmpty()) {
            Pattern pattern = Pattern.compile("\\{([^}]*)}");
            Matcher matcher = pattern.matcher(string);
            if (matcher.find()) {
                String comparator = matcher.group(1);
                if (comparator != null && !comparator.isEmpty()) {
                    Pattern textPattern = Pattern.compile("text\\[([^]]*)]");
                    Matcher textMatcher = textPattern.matcher(comparator);
                    if (textMatcher.find()) {
                        result = textMatcher.group(1);
                    }
                }
            }
        }
        return !result.isEmpty();
    }

    public static boolean isAnimation(String string) {
        boolean returnValue = false;
        if (string != null && !string.isEmpty() && string.contains("{") && string.contains("}")) {
            Pattern pattern = Pattern.compile("\\{([^}]*)}");
            Matcher matcher = pattern.matcher(string);
            if (matcher.find()) {
                String animationString = matcher.group(1);
                if (animationString != null && !animationString.isEmpty()) {
                    for (AnimationType type : AnimationType.values()) {
                        if (animationString.contains(type.getString())) {
                            returnValue = true;
                            break;
                        }
                    }
                }
            }
        }
        return returnValue;
    }

    private void debug() {
        new BukkitRunnable() {
            public void run() {
                if (animators.values().size() > 0) {
                    plugin.getLogger().log(Level.INFO, "-------------");
                    for (String key : animators.keySet()) {
                        plugin.getLogger().log(Level.INFO, key);
                    }
                    plugin.getLogger().log(Level.INFO, "-------------");
                } else {
                    plugin.getLogger().log(Level.INFO, "No Animators");
                }/*
                plugin.getLogger().log(Level.INFO, "***************");
                for (RegisteredListener listener : HandlerList.getRegisteredListeners(plugin)) {
                    plugin.getLogger().log(Level.INFO, listener);
                }
                plugin.getLogger().log(Level.INFO, "***************");*/
            }
        }.runTaskTimerAsynchronously(plugin, 0, 20);
    }

    public void destroy(TextAnimator animator) {
        if (animator != null) {
            animator.destroy();
            animators.remove(animator.getIdentifier());
        }
    }

    public void destroyAll() {
        for (TextAnimator animator : getAnimators().values()) {
            animator.destroy();
        }
        getAnimators().clear();
    }
}
