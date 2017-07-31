package kaka.com.jbox2dapp.widget;

import android.util.Log;
import android.view.View;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import java.util.Random;

import kaka.com.jbox2dapp.R;


/**
 * 世界的实现类
 * Created by kaka on 2017/7/25.
 */
public class JboxImpl {

    private World mWorld; //模拟世界
    private float dt = 1f/60f; //模拟世界的频率
    private int mVelocityIterations = 5; //速率迭代器
    private int mPosiontIterations = 20; //迭代次数
    //世界的长宽
    private int mWidth,mHeight;
    //密度大小
    private float mDesity = 0.5f;
    private float mRatio = 50;//坐标映射比例
    private final Random mRandom = new Random();

    public JboxImpl(float mDesity) {
        this.mDesity = mDesity;
    }

    /**
     * 设置世界的宽高
     * @param width
     * @param height
     */
    public void setWorlSize(int width, int height) {
        mHeight = height;
        mWidth = width;
    }

    /**
     * 开始世界
     */
    public void startWorld(){
        if (mWorld != null) {
            mWorld.step(dt, mVelocityIterations, mPosiontIterations);
        }
    }

    /**
     * 创建世界
     */
    public void createWorld() {
        if (mWorld == null) {
            mWorld = new World(new Vec2(0, 10.0f));
            updateVertiacalBounds();
            updateHorizontalBounds();
        }
    }

    /**
     * 创建世界的上下边界，这里上下边界是一个静止的刚体
     */
    private void updateHorizontalBounds() {
        BodyDef bodyDef = new BodyDef();
        //创建静止刚体
        bodyDef.type = BodyType.STATIC;
        //定义的形状
        PolygonShape box = new PolygonShape();
        float boxWidth = switchPositionToBody(mWidth);
        //设置边界高度为1
        float boxHeight = switchPositionToBody(mRatio);
        box.setAsBox(boxWidth, boxHeight); //确定为矩形

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = box;
        fixtureDef.density = mDesity;
        fixtureDef.friction = 0.8f;//摩擦系数
        fixtureDef.restitution = 0.5f; //补偿系数

        bodyDef.position.set(0, -boxHeight);
        Body topBody = mWorld.createBody(bodyDef); //创建一个真实的上边 body
        topBody.createFixture(fixtureDef);

        bodyDef.position.set(0, switchPositionToBody(mHeight) + boxHeight);
        Body bottomBody = mWorld.createBody(bodyDef);//创建一个真实的下边 body
        bottomBody.createFixture(fixtureDef);
    }
    /**
     * 创建世界的左右边界，这里左右边界是一个静止的刚体
     */
    private void updateVertiacalBounds() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC; //定义静止的刚体

        PolygonShape box = new PolygonShape(); //定义的形状
        float boxWidth = switchPositionToBody(mRatio);
        float boxHeight = switchPositionToBody(mHeight);
        box.setAsBox(boxWidth, boxHeight); //确定为矩形

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = box;
        fixtureDef.density = mDesity;
        fixtureDef.friction = 0.8f;//摩擦系数
        fixtureDef.restitution = 0.5f; //补偿系数

        bodyDef.position.set(-boxWidth, 0);
        Body leftBody = mWorld.createBody(bodyDef); //创建一个真实的上边 body
        leftBody.createFixture(fixtureDef);

        bodyDef.position.set(switchPositionToBody(mWidth) + boxWidth, 0);
        Body rightBody = mWorld.createBody(bodyDef);
        rightBody.createFixture(fixtureDef);
    }

    //创建圆形
    private Shape craeteCircleShape(float radius) {
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(radius);
        return circleShape;
    }

    public void creatBody(View view) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.setType(BodyType.DYNAMIC);

        bodyDef.position.set(switchPositionToBody( view.getX() + (view.getWidth() / 2) )
                ,switchPositionToBody(view.getY() + (view.getHeight() / 2))  );

        Shape shape = null;
        Boolean isCircle = (Boolean) view.getTag(R.id.dn_view_circle_tag);
        if (isCircle != null && isCircle) {
            shape = craeteCircleShape( switchPositionToBody(view.getWidth() / 2) );
        } else {
            Log.i("kaka","createBody veiw tag is not circle!!!");
            return;
        }
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.setShape(shape);
        fixtureDef.friction = 0.8f;//摩擦系数
        fixtureDef.density = mDesity;
        fixtureDef.restitution = 0.5f;//补偿系数

        Body body = mWorld.createBody(bodyDef);
        body.createFixture(fixtureDef);
        view.setTag(R.id.dn_view_body_tag, body);

        body.setLinearVelocity(new Vec2(mRandom.nextFloat(), mRandom.nextFloat()));

    }
    //view坐标映射为物理的坐标
    private float switchPositionToBody(float viewPosition) {
        return viewPosition / mRatio;
    }

    //物理的坐标映射为iew坐标映射
    private float switchPositionToView(float bodyPosition) {
        return bodyPosition * mRatio;
    }
    public boolean isBodyView(View view) {
        Body body = (Body) view.getTag(R.id.dn_view_body_tag);
        return body != null;

    }

    /**
     * 将形状画到屏幕上的屏幕x坐标
     * @param view
     * @return
     */
    public float getViewX(View view) {
        Body body = (Body) view.getTag(R.id.dn_view_body_tag);
        if (body != null) {
            return switchPositionToView(body.getPosition().x ) - (view.getWidth() / 2);
        }
        return 0;
    }
    /**
     * 将形状画到屏幕上的屏幕y坐标
     * @param view
     * @return
     */
    public float getViewY(View view) {
        Body body = (Body) view.getTag(R.id.dn_view_body_tag);
        if (body != null) {
            return switchPositionToView(body.getPosition().y ) - (view.getHeight() / 2);
        }
        return 0;
    }

    /**
     * 获取刚体的旋转弧度
     * @param view
     * @return
     */
    public float getViewRotaion(View view) {
        Body body = (Body) view.getTag(R.id.dn_view_body_tag);
        if (body != null) {
            float angle = body.getAngle();
            return  (angle / 3.14f * 180f) % 360;
        }
        return 0;
    }

    public void applyLinearImpulse(float x, float y, View view) {
        Body body = (Body) view.getTag(R.id.dn_view_body_tag);
        Vec2 impluse = new Vec2(x,y);
        body.applyLinearImpulse(impluse, body.getPosition(), true); //给body做线性运动 true 运动完之后停止
    }

}
