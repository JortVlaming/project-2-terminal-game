package me.JortVlaming.entity;

import com.crystalcoding.pathfinding.Point;
import com.crystalcoding.pathfinding.PointPath;
import me.JortVlaming.game.GamePanel;
import me.JortVlaming.game.GameState;
import me.JortVlaming.game.Util;
import me.JortVlaming.monster.HostileEntity;
import org.apache.commons.lang3.ArrayUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public abstract class Entity {
    // CONFIG STUFF
    public GamePanel gp;
    public int worldX, worldY;
    public int speed;

    // SPRITE STUFF
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public int direction; // 0 = up, 1 = right, 2 = down, 3 = left
    public int spriteCounter = 0;
    public int spriteNum = 1;

    // COLLISION STUFF
    public Rectangle solidArea;
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn;

    // ACTION STUFF
    public int actionLockTimer = 0;

    // CHARACTER STAT STUFF
    public int maxLife;
    public int life;

    // RENDERING STUFF
    protected int spriteWidth, spriteHeight;
    int spriteOffsetX = 0, spriteOffsetY = 0;

    // PATHING STUFF
    PointPath currentPointPath;
    int currentPointIndex = 0;
    boolean lockedToPlayer = false;
    Color pathColor = new Color((float) Math.random(), (float) Math.random(), (float) Math.random(), 1f);

    public Entity(GamePanel gp) {
        this.gp = gp;

        spriteWidth = gp.getTileSize();
        spriteHeight = gp.getTileSize();

        solidArea = new Rectangle(0, 0, gp.getTileSize(), gp.getTileSize());
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        loadImages();
    }

    public abstract void loadImages();

    public abstract void update();

    public void speak() {
        gp.currentState = GameState.DIALOGUE;

        switch (gp.getPlayer().direction) {
            default:
            case 0: {
                direction = 2;
                break;
            }
            case 1: {
                direction = 3;
                break;
            }
            case 2: {
                direction = 0;
                break;
            }
            case 3: {
                direction = 1;
                break;
            }
        }
    }

    public void incrementSpriteCounter() {
        spriteCounter++;
        if (spriteCounter > 15 || (this instanceof Player && gp.getPlayer().attacking && spriteCounter > 7)) {
            spriteNum = spriteNum == 1 ? 2 : 1;
            spriteCounter = 0;
        }
    }

    public BufferedImage getCurrentImage() {
        BufferedImage image = null;

        switch (direction) {
            case 0: {
                image = spriteNum == 1 ? up1 : up2;
                break;
            }
            case 1: {
                image = spriteNum == 1 ? right1 : right2;
                break;
            }
            case 2: {
                image = spriteNum == 1 ? down1 : down2;
                break;
            }
            case 3: {
                image = spriteNum == 1 ? left1 : left2;
                break;
            }
        }

        return image;
    }

    public void draw(Graphics2D g2D) {
        BufferedImage image = getCurrentImage();

        if (image == null) return;

        if (!Util.isOnScreen(worldX, worldY, gp)) {
            return;
        }
        int screenX = worldX - gp.getPlayer().worldX + gp.getPlayer().screenX;
        int screenY = worldY - gp.getPlayer().worldY + gp.getPlayer().screenY;

        g2D.drawImage(image, screenX + spriteOffsetX, screenY + spriteOffsetY, spriteWidth, spriteHeight, null);

        if (GamePanel.DEBUG) {
            g2D.setColor(Color.RED);
            g2D.fillRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
            if (this instanceof Player) {
                Player p = (Player) this;

                if (p.isAttackColliderActive()) {
                    g2D.setColor(Color.BLUE);
                    g2D.fillRect(screenX + p.attackCollisionArea.x, screenY + p.attackCollisionArea.y, p.attackCollisionArea.width, p.attackCollisionArea.height);
                }
            }
            g2D.setColor(Color.WHITE);
            FontMetrics fm = g2D.getFontMetrics();
            g2D.setFont(new Font("Arial", Font.PLAIN, 20));
            Rectangle2D bounds = fm.getStringBounds(direction + "/" + life, g2D);
            g2D.drawString(direction + "/" + life, (int) (screenX + solidArea.x + (double) solidArea.width / 2 - bounds.getWidth()/2), (int) (screenY + solidArea.y + bounds.getHeight()/2 + (double) solidArea.height / 2));
        }
    }

    public BufferedImage loadImage(String imageName, String IMAGE_PATH) {
        try {
            InputStream is = getClass().getResourceAsStream(IMAGE_PATH + imageName);
            if (is != null) return ImageIO.read(is);
            System.out.println("Failed to load image: " + imageName + " from path: " + IMAGE_PATH);
            return null;
        } catch (IOException e) {
            throw new RuntimeException("Could not load image at path: " + IMAGE_PATH + imageName, e);
        }
    }

    public void moveWithCurrentDirection() {
        if (!collisionOn) {
            switch (direction) {
                case 0:
                default: {
                    worldY -= speed;
                    break;
                }
                case 1: {
                    worldX += speed;
                    break;
                }
                case 2: {
                    worldY += speed;
                    break;
                }
                case 3: {
                    worldX -= speed;
                    break;
                }
            }
        }
    }

    public void takeDamage(int i) {
        life -= i;

        if (!(this instanceof Player) && life <= 0) {
            System.out.println(this.getClass().getSimpleName() + " DIED!!!!");
            if (!(this instanceof HostileEntity))
                gp.getEntityManager().removeEntityFromWorld(this);
        } else if (this instanceof Player && life <= 0) {
            // TODO death
        }
    }

    public void setAction() {
        if (currentPointPath == null || lockedToPlayer) {
            Random r = new Random();

            int currentX = Util.worldXToGridX(worldX);
            int currentY = Util.worldYToGridY(worldY);

            int goalX;
            int goalY;

            if (lockedToPlayer) {
                goalX = Util.worldXToGridX(gp.getPlayer().worldX);
                goalY = Util.worldYToGridY(gp.getPlayer().worldY);
            } else {
                goalX = r.nextInt(currentX-5, currentX+5);
                goalY = r.nextInt(currentY-5, currentY+5);
            }

            currentPointPath = null;
            currentPointPath = gp.getPathfindingManager().find(currentX, currentY, goalX, goalY);
            currentPointIndex = 0;
            if (currentPointPath != null) {
                ArrayUtils.reverse(currentPointPath.points());
            }
        }

        if (currentPointPath == null) return;

        if (Util.getDistanceFromPoint(this, currentPointPath.points()[currentPointIndex]) < 1) {
            currentPointIndex++;

            if (currentPointIndex >= currentPointPath.points().length) {
                currentPointPath = null;
                return;
            }
        }

        Point currentPoint = currentPointPath.points()[currentPointIndex];

        int wX = Util.gridXToWorldX(currentPoint.x());
        int wY = Util.gridYToWorldY(currentPoint.y());

        if (wY < worldY) {
            // point is above the entity
            direction = 0;
            // System.out.println(currentPoint + " Up");
        } else if (wY > worldY) {
            // point is below the entity
            direction = 2;
            // System.out.println(currentPoint + " Down");
        } else if (wX > worldX) {
            // point is right of the entity
            direction = 1;
            // System.out.println(currentPoint + " Right");
        } else if (wX < worldX) {
            // point is left of the entity
            direction = 3;
            // System.out.println(currentPoint + " Left");
        }
    }

    public PointPath getCurrentPointPath() {
        return currentPointPath;
    }

    public int getCurrentPointIndex() {
        return currentPointIndex;
    }

    public Color getPointColor() {
        return pathColor;
    }
}
