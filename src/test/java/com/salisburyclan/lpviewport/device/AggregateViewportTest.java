package com.salisburyclan.lpviewport.device;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.truth.Truth8;

import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.api.ViewExtent;
import com.salisburyclan.lpviewport.device.AggregateViewport;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class AggregateViewportTest {

  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);
  @Mock private Viewport mockViewport1;
  @Mock private Viewport mockViewport2;

  private AggregateViewport viewport;

  @Before
  public void setUp() {
    // Two 10x20 Viewports, attached left-to-right.
    when(mockViewport1.getExtent()).thenReturn(new ViewExtent(0, 0, 9, 19));
    when(mockViewport2.getExtent()).thenReturn(new ViewExtent(10, 10, 19, 29));

    AggregateViewport.Builder builder = new AggregateViewport.Builder();
    builder.add(mockViewport1, 0, 0);
    builder.add(mockViewport2, 10, 0);
    viewport = builder.build();
  }

  @Test
  public void testGetExtent() throws Exception {
    assertThat(viewport.getExtent()).isEqualTo(new ViewExtent(0, 0, 19, 19));
  }

  @Test
  public void testSetLight() throws Exception {
    Color color = Color.RED;
    viewport.setLight(0, 0, color);
    viewport.setLight(9, 19, color);
    verify(mockViewport1).setLight(0, 0, color);
    verify(mockViewport1).setLight(9, 19, color);

    viewport.setLight(10, 0, color);
    viewport.setLight(19, 19, color);
    verify(mockViewport2).setLight(10, 10, color);
    verify(mockViewport2).setLight(19, 29, color);
  }

  @Test
  public void testAddListener() throws Exception {
    // TODO(mpsalisbury): implement
  }
}
