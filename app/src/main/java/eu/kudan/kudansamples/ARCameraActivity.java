package eu.kudan.kudansamples;

//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.List;

import eu.kudan.kudan.ARActivity;
import eu.kudan.kudan.ARAlphaVideoNode;
import eu.kudan.kudan.ARImageNode;
import eu.kudan.kudan.ARImageTrackable;
import eu.kudan.kudan.ARImageTrackableListener;
import eu.kudan.kudan.ARImageTracker;
import eu.kudan.kudan.ARLightMaterial;
import eu.kudan.kudan.ARMeshNode;
import eu.kudan.kudan.ARModelImporter;
import eu.kudan.kudan.ARModelNode;
import eu.kudan.kudan.ARNode;
import eu.kudan.kudan.ARRenderer;
import eu.kudan.kudan.ARTexture2D;
import eu.kudan.kudan.ARTextureMaterial;
import eu.kudan.kudan.ARVideoNode;
import eu.kudan.kudan.ARVideoTexture;
import eu.kudan.kudan.ARView;

public class ARCameraActivity extends ARActivity {

    private ARImageTrackable trackableApple,trackableBanana,trackableGrapes,trackable;
    ARImageNode imageNode;
    ARModelNode modelNode;
    RelativeLayout relative;
    float dX,dY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arcamera);
       relative= (RelativeLayout) findViewById(R.id.relativeLayout);

        relative.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        dX = view.getX() - event.getRawX();
                        dY = view.getY() - event.getRawY();

                        break;

                    case MotionEvent.ACTION_MOVE:


                        float newX = event.getRawX() + dX;
                        float newY= event.getRawY() + dY;
                        rotate(newX,newY);

                        break;
                    default:
                        return false;
                }
                return true;
            }
        });



    }

    private void rotate(float newX, float newY) {

        imageNode.rotateByDegrees(newY/10,1,0,0);
        imageNode.rotateByDegrees(newX/10,0,1,0);
        modelNode.rotateByDegrees(newY/10,1,0,0);
        modelNode.rotateByDegrees(newX/10,0,1,0);
    }

    public void setup() {
        addImageTrackable();
        addImageNode();
        addVideoNode();
//        addAlphaVideoNode();
        addModelNode();
    }

    private void addImageTrackable() {

        // Initialise image trackable
        trackableApple = new ARImageTrackable("apple");
        trackableApple.loadFromAsset("RedApple.jpg");
        // Get instance of image tracker manager
        ARImageTracker trackableManager = ARImageTracker.getInstance();
        // Add image trackable to image tracker manager
        trackableManager.addTrackable(trackableApple);


        trackableBanana = new ARImageTrackable("banana");
        trackableApple.loadFromAsset("banana.jpg");
        trackableManager.addTrackable(trackableApple);


        trackableGrapes = new ARImageTrackable("grapes");
        trackableApple.loadFromAsset("Grapes.jpg");
        trackableManager.addTrackable(trackableApple);
    }

    private void addModelNode() {
        // Import model
        ARModelImporter modelImporter = new ARModelImporter();
        modelImporter.loadFromAsset("ben.jet");
        modelNode = (ARModelNode)modelImporter.getNode();


        // Load model texture
        ARTexture2D texture2D = new ARTexture2D();
        texture2D.loadFromAsset("bigBenTexture.png");

        // Apply model texture to model texture material
        ARLightMaterial material = new ARLightMaterial();
        material.setTexture(texture2D);
        material.setAmbient(0.8f, 0.8f, 0.8f);

        // Apply texture material to models mesh nodes
        for(ARMeshNode meshNode : modelImporter.getMeshNodes()){
            meshNode.setMaterial(material);
        }


        modelNode.rotateByDegrees(90,1,0,0);
        modelNode.scaleByUniform(0.25f);

        // Add model node to image trackable
        trackable.getWorld().addChild(modelNode);
        modelNode.setVisible(true);

    }

//    private void addAlphaVideoNode() {
//
//        // Initialise video texture
//        ARVideoTexture videoTexture = new ARVideoTexture();
//        videoTexture.loadFromAsset("kaboom.mp4");
//
//        // Initialise alpha video node with video texture
//        ARAlphaVideoNode alphaVideoNode = new ARAlphaVideoNode(videoTexture);
//
//        // Add alpha video node to image trackable
//        trackable.getWorld().addChild(alphaVideoNode);
//
//        // Alpha video scale
//        float scale = trackable.getWidth() / videoTexture.getWidth();
//        alphaVideoNode.scaleByUniform(scale);
//
//        alphaVideoNode.setVisible(false);
//
//    }

    private void addVideoNode() {

        // Initialise video texture
        ARVideoTexture videoTexture = new ARVideoTexture();
        videoTexture.loadFromAsset("waves.mp4");

        // Initialise video node with video texture
        ARVideoNode videoNode = new ARVideoNode(videoTexture);

        //Add video node to image trackable
        trackable.getWorld().addChild(videoNode);

        // Video scale
        float scale = trackable.getWidth() / videoTexture.getWidth();
        videoNode.scaleByUniform(scale);

        videoNode.setVisible(false);

    }

    private void addImageNode() {

        imageNode = new ARImageNode("transparent-yes.png");
        trackableApple.getWorld().addChild(imageNode);
        ARTextureMaterial textureMaterial = (ARTextureMaterial)imageNode.getMaterial();
        float scale = trackableApple.getWidth() / textureMaterial.getTexture().getWidth();
        imageNode.scaleByUniform(scale);
        imageNode.setVisible(false);

        imageNode = new ARImageNode("transparent-no.png");
        trackableBanana.getWorld().addChild(imageNode);
        scale = trackableBanana.getWidth() / textureMaterial.getTexture().getWidth();
        imageNode.scaleByUniform(scale);
        imageNode.setVisible(false);

        imageNode = new ARImageNode("transparent-no.png");
        trackableGrapes.getWorld().addChild(imageNode);
        scale = trackableGrapes.getWidth() / textureMaterial.getTexture().getWidth();
        imageNode.scaleByUniform(scale);
        imageNode.setVisible(false);

    }


    public void addModelButtonPressed() {

        hideAll();
        trackable.getWorld().getChildren().get(3).setVisible(true);
    }

    public void addAlphaButtonPressed() {

        hideAll();
        trackable.getWorld().getChildren().get(2).setVisible(true);

    }

    public void addVideoButtonPressed() {

        hideAll();
        trackable.getWorld().getChildren().get(1).setVisible(true);
    }

    public void addImageButtonPressed() {
        hideAll();
        trackable.getWorld().getChildren().get(0).setVisible(true);
    }


    private void hideAll(){
       List<ARNode> nodes =  trackable.getWorld().getChildren();
        for(ARNode node : nodes){
            node.setVisible(false);
        }
    }
}
