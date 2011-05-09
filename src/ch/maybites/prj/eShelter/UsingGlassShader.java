package ch.maybites.prj.eShelter;


import javax.media.opengl.GL;

import gestalt.G;
import gestalt.candidates.glsl.ShaderManager;
import gestalt.candidates.glsl.ShaderProgram;
import gestalt.context.DisplayCapabilities;
import gestalt.context.GLContext;
import gestalt.model.Model;
import gestalt.model.ModelData;
import gestalt.model.ModelLoaderOBJ;
import gestalt.render.AnimatorRenderer;
import gestalt.shape.Material;
import gestalt.shape.Plane;
import gestalt.shape.material.MaterialPlugin;
import gestalt.shape.material.TexturePlugin;
import gestalt.texture.Bitmaps;


public class UsingGlassShader
    extends AnimatorRenderer {

    private ShaderManager _myShaderManager;

    private ShaderProgram _myShaderProgram;

    public void setup() {
    	
    	GlobalPreferences.getInstance().setDataPath("/Users/maf/Arbeiten/02_code/eclipse/Prj_eShelterBoids/data/");
        /* setup camera */
        cameramover(true);
        camera().setMode(CAMERA_MODE_LOOK_AT);

        /* create shadermanager and a shaderprogram */
        _myShaderManager = drawablefactory().extensions().shadermanager();
        _myShaderProgram = _myShaderManager.createShaderProgram();
        _myShaderManager.attachVertexShader(_myShaderProgram, GlobalPreferences.getInstance().getStream("shader/RefractionReflectionShader.vs"));
        _myShaderManager.attachFragmentShader(_myShaderProgram, GlobalPreferences.getInstance().getStream("shader/RefractionReflectionShader.fs"));
        bin(BIN_FRAME_SETUP).add(_myShaderManager);

        /* create textures */
        TexturePlugin myRefractionTexture = drawablefactory().texture();
        myRefractionTexture.load(Bitmaps.getBitmap(GlobalPreferences.getInstance().getAbsDataPath("shader/images/cube.png")));
        myRefractionTexture.setTextureUnit(GL.GL_TEXTURE1);

        TexturePlugin myReflectionTexture = drawablefactory().texture();
        myReflectionTexture.load(Bitmaps.getBitmap(GlobalPreferences.getInstance().getAbsDataPath("shader/images/sky-reflection.png")));
        myReflectionTexture.setTextureUnit(GL.GL_TEXTURE0);

        /* create model */
        ModelData myModelData = ModelLoaderOBJ.getModelData(GlobalPreferences.getInstance().getStream("resource/model/singleE_lowPolyVolume.obj"));
        myModelData.averageNormals();
        Model myModel = G.model(myModelData);
        myModel.mesh().scale().scale(50);
        myModel.mesh().material().lit = true;
        myModel.mesh().material().addPlugin(myRefractionTexture);
        myModel.mesh().material().addPlugin(myReflectionTexture);
        myModel.mesh().material().addPlugin(new ShaderMaterial());

        /* create background */
        Plane myPlane = G.plane(bin(BIN_2D_BACKGROUND), GlobalPreferences.getInstance().getAbsDataPath("shader/images/cube.png"));
        myPlane.setPlaneSizeToTextureSize();
        myPlane.rotation().set(PI, 0, 0);
        myPlane.material().depthmask = false;

    }


    public void loop(final float theDeltaTime) {
        light().position().set(camera().position());
        light().position().z = event().mouseY;
    }


    private class ShaderMaterial
        implements MaterialPlugin {

        public void begin(GLContext theRenderContext, Material theParent) {
            /* enable shader */
            _myShaderManager.enable(_myShaderProgram);

            /* set uniform variables in shader */
            _myShaderManager.setUniform(_myShaderProgram, "LightPos", 0.0f, 0.0f, 4.0f);
            _myShaderManager.setUniform(_myShaderProgram, "BaseColor", 1.0f, 1.0f, 1.0f);
            _myShaderManager.setUniform(_myShaderProgram, "EnvMap", 0);
            _myShaderManager.setUniform(_myShaderProgram, "RefractionMap", 1);
            _myShaderManager.setUniform(_myShaderProgram, "textureWidth", 512.0f);
            _myShaderManager.setUniform(_myShaderProgram, "textureHeight", 512.0f);

            _myShaderManager.setUniform(_myShaderProgram, "Depth", 0f);// event().normalized_mouseX);
            _myShaderManager.setUniform(_myShaderProgram, "MixRatio", 0f);//event().normalized_mouseY);
        }


        public void end(GLContext theRenderContext, Material theParent) {
            _myShaderManager.disable();
        }
    }


    public static void main(String[] args) {
        DisplayCapabilities myDisplayCapabilities = new DisplayCapabilities();
        myDisplayCapabilities.width = 512;
        myDisplayCapabilities.height = 512;
        myDisplayCapabilities.backgroundcolor.set(1);
        G.init(UsingGlassShader.class, myDisplayCapabilities);
    }
}
