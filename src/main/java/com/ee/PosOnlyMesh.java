package com.ee;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.security.InvalidParameterException;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL33.*;

import org.lwjgl.BufferUtils;

public class PosOnlyMesh implements AutoCloseable, Mesh {
    public int vao, vbo, ebo, indexCount;

    public PosOnlyMesh(ArrayList<PosOnlyMeshVertex> vertices, ArrayList<Integer> indices)
            throws InvalidParameterException {
        indexCount = indices.size();

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertices.size() * 3);
        for (PosOnlyMeshVertex v : vertices) {
            vertexBuffer.put(v.position().x).put(v.position().y).put(v.position().z);
        }
        vertexBuffer.flip();
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        IntBuffer indexBuffer = BufferUtils.createIntBuffer(indices.size());
        for (int idx : indices)
            indexBuffer.put(idx);
        indexBuffer.flip();

        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        ebo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * 4, 0);
        glEnableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    @Override
    public void close() {
        glDeleteBuffers(ebo);
        glDeleteBuffers(vbo);
        glDeleteVertexArrays(vao);
    }

    public void bind() {
        glBindVertexArray(vao);
    }

    public void unbind() {
        glBindVertexArray(0);
    }

    public void draw() {
        glDrawElements(GL_TRIANGLES, indexCount, GL_UNSIGNED_INT, 0);
    }
}
