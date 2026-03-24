package com.ee.Client;

import static org.lwjgl.opengl.GL33.*;

import org.joml.Matrix4f;

public class Shader implements AutoCloseable {
    private int programID;

    public Shader(String vertexSource, String fragmentSource) throws ShaderException {
        int vert = glCreateShader(GL_VERTEX_SHADER);
        int frag = glCreateShader(GL_FRAGMENT_SHADER);

        try {
            compileShader(vert, vertexSource);
            compileShader(frag, fragmentSource);
        } catch (ShaderException e) {
            glDeleteShader(frag);
            glDeleteShader(vert);
            throw new ShaderException(e);
        }

        try {
            programID = glCreateProgram();
            linkShader(programID, vert, frag);
        } catch (ShaderException e) {
            glDeleteProgram(programID);
            glDeleteShader(frag);
            glDeleteShader(vert);
            throw new ShaderException(e);
        }

        glDeleteShader(frag);
        glDeleteShader(vert);
    }

    public void use() {
        glUseProgram(programID);
    }

    public void setUniform1i(String name, int value) {
        int location = glGetUniformLocation(programID, name);
        glUniform1i(location, value);
    }

    public void setUniformMatrix4f(String name, Matrix4f matrix) {
        int location = glGetUniformLocation(programID, name);
        glUniformMatrix4fv(location, false, matrix.get(new float[16]));
    }

    @Override
    public void close() {
        glDeleteProgram(programID);
    }

    private void compileShader(int shaderID, String source) throws ShaderException {
        glShaderSource(shaderID, source);
        glCompileShader(shaderID);
        if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE) {
            var log = glGetShaderInfoLog(shaderID);
            throw new ShaderException("Failed to compile shader: " + log);
        }
    }

    private void linkShader(int program, int vertex, int fragment) {
        glAttachShader(program, vertex);
        glAttachShader(program, fragment);
        glLinkProgram(program);
        if (glGetProgrami(program, GL_LINK_STATUS) == GL_FALSE) {
            var log = glGetProgramInfoLog(program);
            throw new ShaderException("Failed to compile shader: " + log);
        }
    }
}
