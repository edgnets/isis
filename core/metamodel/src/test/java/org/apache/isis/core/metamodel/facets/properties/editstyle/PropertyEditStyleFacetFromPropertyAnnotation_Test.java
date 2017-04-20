package org.apache.isis.core.metamodel.facets.properties.editstyle;

import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyEditStyle;
import org.apache.isis.core.commons.config.IsisConfiguration;
import org.apache.isis.core.commons.matchers.IsisMatchers;
import org.apache.isis.core.metamodel.facetapi.FacetHolder;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;

public class PropertyEditStyleFacetFromPropertyAnnotation_Test {

    @Rule
    public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(JUnitRuleMockery2.Mode.INTERFACES_AND_CLASSES);

    @Mock
    IsisConfiguration mockConfiguration;

    @Mock
    FacetHolder mockFacetHolder;

    @Mock
    Property mockProperty;


    public static class Create_Test extends PropertyEditStyleFacetFromPropertyAnnotation_Test {

        @Test
        public void when_annotated_with_dialog() throws Exception {

            context.checking(new Expectations() {{
                allowing(mockProperty).editStyle();
                will(returnValue(PropertyEditStyle.DIALOG));

                never(mockConfiguration);
            }});

            PropertyEditStyleFacet facet = PropertyEditStyleFacetFromPropertyAnnotation
                    .create(mockProperty, mockConfiguration, mockFacetHolder);

            Assert.assertThat(facet, is((Matcher) IsisMatchers.anInstanceOf(PropertyEditStyleFacetFromPropertyAnnotation.class)));
            Assert.assertThat(facet.editStyle(), is(PropertyEditStyle.DIALOG));
        }

        @Test
        public void when_annotated_with_inline() throws Exception {

            context.checking(new Expectations() {{
                allowing(mockProperty).editStyle();
                will(returnValue(PropertyEditStyle.INLINE));

                never(mockConfiguration);
            }});


            PropertyEditStyleFacet facet = PropertyEditStyleFacetFromPropertyAnnotation
                    .create(mockProperty, mockConfiguration, mockFacetHolder);

            Assert.assertThat(facet, is((Matcher) IsisMatchers.anInstanceOf(PropertyEditStyleFacetFromPropertyAnnotation.class)));
            Assert.assertThat(facet.editStyle(), is(PropertyEditStyle.INLINE));
        }

        @Test
        public void when_annotated_with_as_configured() throws Exception {

            context.checking(new Expectations() {{
                allowing(mockProperty).editStyle();
                will(returnValue(PropertyEditStyle.AS_CONFIGURED));

                oneOf(mockConfiguration).getString("isis.properties.editStyle");
                will(returnValue(PropertyEditStyle.INLINE.name()));

                allowing(mockFacetHolder).containsDoOpFacet(PropertyEditStyleFacet.class);
                will(returnValue(false));
            }});

            PropertyEditStyleFacet facet = PropertyEditStyleFacetFromPropertyAnnotation
                    .create(mockProperty, mockConfiguration, mockFacetHolder);

            Assert.assertThat(facet, is((Matcher) IsisMatchers.anInstanceOf(PropertyEditStyleFacetAsConfigured.class)));
            Assert.assertThat(facet.editStyle(), is(PropertyEditStyle.INLINE));
        }

        @Test
        public void when_annotated_with_as_configured_but_already_has_doop_facet() throws Exception {

            context.checking(new Expectations() {{
                allowing(mockProperty).editStyle();
                will(returnValue(PropertyEditStyle.AS_CONFIGURED));

                oneOf(mockFacetHolder).containsDoOpFacet(PropertyEditStyleFacet.class);
                will(returnValue(true));

                never(mockConfiguration);
            }});

            PropertyEditStyleFacet facet = PropertyEditStyleFacetFromPropertyAnnotation
                    .create(mockProperty, mockConfiguration, mockFacetHolder);

            Assert.assertThat(facet, is(nullValue()));
        }

        @Test
        public void when_not_annotated() throws Exception {

            context.checking(new Expectations() {{
                allowing(mockProperty).editStyle();
                will(returnValue(null));

                allowing(mockFacetHolder).containsDoOpFacet(PropertyEditStyleFacet.class);
                will(returnValue(false));

                never(mockConfiguration);
            }});

            PropertyEditStyleFacet facet = PropertyEditStyleFacetFromPropertyAnnotation
                    .create(mockProperty, mockConfiguration, mockFacetHolder);

            Assert.assertThat(facet.editStyle(), is(PropertyEditStyle.DIALOG));
            Assert.assertThat(facet, is((Matcher) IsisMatchers.anInstanceOf(PropertyEditStyleFacetFallBack.class)));
        }

        @Test
        public void when_not_annotated_but_already_has_doop_facet() throws Exception {

            context.checking(new Expectations() {{
                allowing(mockProperty).editStyle();
                will(returnValue(null));

                allowing(mockFacetHolder).containsDoOpFacet(PropertyEditStyleFacet.class);
                will(returnValue(true));

                never(mockConfiguration);
            }});

            PropertyEditStyleFacet facet = PropertyEditStyleFacetFromPropertyAnnotation
                    .create(mockProperty, mockConfiguration, mockFacetHolder);

            Assert.assertThat(facet, is(nullValue()));
        }


    }

}
