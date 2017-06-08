package com.salisburyclan.lpviewport.geom;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class EdgeTest {

  @Test
  public void testOpposite() {
    assertThat(Edge.LEFT.getOpposite()).isEqualTo(Edge.RIGHT);
    assertThat(Edge.RIGHT.getOpposite()).isEqualTo(Edge.LEFT);
    assertThat(Edge.TOP.getOpposite()).isEqualTo(Edge.BOTTOM);
    assertThat(Edge.BOTTOM.getOpposite()).isEqualTo(Edge.TOP);
    assertThat(Edge.INVALID.getOpposite()).isEqualTo(Edge.INVALID);
  }
}
