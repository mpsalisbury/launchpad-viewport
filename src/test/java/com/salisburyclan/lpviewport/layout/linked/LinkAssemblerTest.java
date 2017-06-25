package com.salisburyclan.lpviewport.layout.linked;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.salisburyclan.lpviewport.api.RawLayer;
import com.salisburyclan.lpviewport.api.RawViewport;
import com.salisburyclan.lpviewport.geom.Edge;
import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;
import com.salisburyclan.lpviewport.layer.DColor;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;

@RunWith(JUnit4.class)
public class LinkAssemblerTest {

  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);
  @Mock private RawViewport mockViewport1;
  @Mock private RawViewport mockViewport2;
  @Mock private RawLayer mockRawLayer1;
  @Mock private RawLayer mockRawLayer2;
  private static final DColor COLOR = DColor.RED;

  private RawViewport linkedViewport;

  @Before
  public void setUp() {
    // Two 10x10 Viewports, attached left-to-right.
    when(mockViewport1.getExtent()).thenReturn(Range2.create(0, 0, 9, 9));
    when(mockViewport2.getExtent()).thenReturn(Range2.create(0, 0, 9, 9));
    when(mockViewport1.getRawLayer()).thenReturn(mockRawLayer1);
    when(mockViewport2.getRawLayer()).thenReturn(mockRawLayer2);

    LinkAssembler assembler = new LinkAssembler();
    assembler.addLink(
        new Link(mockViewport1, Edge.RIGHT, Point.create(9, 5), mockViewport2, Point.create(0, 5)));
    linkedViewport = assembler.build();
  }

  // Test that when we set lights on the aggregate viewport, the correct
  // sub-viewport lights get set.
  @Test
  public void testOffset() throws Exception {
    linkedViewport.getRawLayer().setPixel(Point.create(0, 0), COLOR);
    linkedViewport.getRawLayer().setPixel(Point.create(9, 9), COLOR);
    verify(mockRawLayer1).setPixel(Point.create(0, 0), COLOR);
    verify(mockRawLayer1).setPixel(Point.create(9, 9), COLOR);

    linkedViewport.getRawLayer().setPixel(Point.create(10, 0), COLOR);
    linkedViewport.getRawLayer().setPixel(Point.create(19, 9), COLOR);
    verify(mockRawLayer2).setPixel(Point.create(0, 0), COLOR);
    verify(mockRawLayer2).setPixel(Point.create(9, 9), COLOR);
  }
}
