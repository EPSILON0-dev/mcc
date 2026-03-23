package com.ee;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.security.InvalidParameterException;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL33.*;

import org.lwjgl.BufferUtils;

public class ChunkMesh implements AutoCloseable, Mesh {
    public int vao, vbo, ebo, indexCount;

    public ChunkMesh(ArrayList<ChunkMeshVertex> vertices, ArrayList<Integer> indices) throws InvalidParameterException {
        if (vertices == null || vertices.isEmpty()) {
            throw new InvalidParameterException("Vertices cannot be null or empty");
        }

        if (indices == null || indices.isEmpty()) {
            throw new InvalidParameterException("Indices cannot be null or empty");
        }

        if (indices.size() % 3 != 0) {
            throw new InvalidParameterException("Indices size must be a multiple of 3 for triangles");
        }

        indexCount = indices.size();

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertices.size() * 9);
        for (ChunkMeshVertex v : vertices) {
            vertexBuffer.put(v.position().x).put(v.position().y).put(v.position().z);
            vertexBuffer.put(v.normal().x).put(v.normal().y).put(v.normal().z);
            vertexBuffer.put(v.texCoord().x).put(v.texCoord().y);
            vertexBuffer.put(v.textureIndex());
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

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 9 * 4, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 9 * 4, (3) * 4);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 9 * 4, (3 + 3) * 4);
        glEnableVertexAttribArray(2);
        glVertexAttribPointer(3, 1, GL_FLOAT, false, 9 * 4, (3 + 3 + 2) * 4);
        glEnableVertexAttribArray(3);

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
