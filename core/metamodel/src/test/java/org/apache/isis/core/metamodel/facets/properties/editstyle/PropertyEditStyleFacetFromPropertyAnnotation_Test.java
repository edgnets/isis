package org.apache.isis.core.metamodel.facets.properties.editstyle;

import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import org.apache.isis.applib.annotation.PromptStyle;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.core.commons.config.IsisConfiguration;
import org.apache.isis.core.commons.matchers.IsisMatchers;
import org.apache.isis.core.metamodel.facetapi.FacetHolder;
import org.apache.isis.core.metamodel.facets.object.promptStyle.PromptStyleFacet;
import org.apache.isis.core.metamodel.facets.object.promptStyle.PromptStyleFacetAsConfigured;
import org.apache.isis.core.metamodel.facets.object.promptStyle.PromptStyleFacetFallBack;
import org.apache.isis.core.metamodel.facets.properties.propertylayout.PromptStyleFacetForPropertyLayoutAnnotation;
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
    PropertyLayout mockPropertyLayout;


    public static class Create_Test extends PropertyEditStyleFacetFromPropertyAnnotation_Test {

        @Test
        public void when_annotated_with_dialog() throws Exception {

            context.checking(new Expectations() {{
                allowing(mockPropertyLayout).promptStyle();
                will(returnValue(PromptStyle.DIALOG));

                never(mockConfiguration);
            }});

            PromptStyleFacet facet = PromptStyleFacetForPropertyLayoutAnnotation
                    .create(mockPropertyLayout, mockConfiguration, mockFacetHolder);

            Assert.assertThat(facet, is((Matcher) IsisMatchers.anInstanceOf(PromptStyleFacetForPropertyLayoutAnnotation.class)));
            Assert.assertThat(facet.value(), is(PromptStyle.DIALOG));
        }

        @Test
        public void when_annotated_with_inline() throws Exception {

            context.checking(new Expectations() {{
                allowing(mockPropertyLayout).promptStyle();
                will(returnValue(PromptStyle.INLINE));

                never(mockConfiguration);
            }});


            PromptStyleFacet facet = PromptStyleFacetForPropertyLayoutAnnotation
                    .create(mockPropertyLayout, mockConfiguration, mockFacetHolder);

            Assert.assertThat(facet, is((Matcher) IsisMatchers.anInstanceOf(PromptStyleFacetForPropertyLayoutAnnotation.class)));
            Assert.assertThat(facet.value(), is(PromptStyle.INLINE));
        }

        @Test
        public void when_annotated_with_as_configured() throws Exception {

            context.checking(new Expectations() {{
                allowing(mockPropertyLayout).promptStyle();
                will(returnValue(PromptStyle.AS_CONFIGURED));

                oneOf(mockConfiguration).getString("isis.properties.editStyle");
                will(returnValue(PromptStyle.INLINE.name()));

                allowing(mockFacetHolder).containsDoOpFacet(PromptStyleFacet.class);
                will(returnValue(false));
            }});

            PromptStyleFacet facet = PromptStyleFacetForPropertyLayoutAnnotation
                    .create(mockPropertyLayout, mockConfiguration, mockFacetHolder);

            Assert.assertThat(facet, is((Matcher) IsisMatchers.anInstanceOf(PromptStyleFacetAsConfigured.class)));
            Assert.assertThat(facet.value(), is(PromptStyle.INLINE));
        }

        @Test
        public void when_annotated_with_as_configured_but_already_has_doop_facet() throws Exception {

            context.checking(new Expectations() {{
                allowing(mockPropertyLayout).promptStyle();
                will(returnValue(PromptStyle.AS_CONFIGURED));

                oneOf(mockFacetHolder).containsDoOpFacet(PromptStyleFacet.class);
                will(returnValue(true));

                never(mockConfiguration);
            }});

            PromptStyleFacet facet = PromptStyleFacetForPropertyLayoutAnnotation
                    .create(mockPropertyLayout, mockConfiguration, mockFacetHolder);

            Assert.assertThat(facet, is(nullValue()));
        }

        @Test
        public void when_not_annotated() throws Exception {

            context.checking(new Expectations() {{
                allowing(mockPropertyLayout).promptStyle();
                will(returnValue(null));

                allowing(mockFacetHolder).containsDoOpFacet(PromptStyleFacet.class);
                will(returnValue(false));

                never(mockConfiguration);
            }});

            PromptStyleFacet facet = PromptStyleFacetForPropertyLayoutAnnotation
                    .create(mockPropertyLayout, mockConfiguration, mockFacetHolder);

            Assert.assertThat(facet.value(), is(PromptStyle.DIALOG));
            Assert.assertThat(facet, is((Matcher) IsisMatchers.anInstanceOf(PromptStyleFacetFallBack.class)));
        }

        @Test
        public void when_not_annotated_but_already_has_doop_facet() throws Exception {

            context.checking(new Expectations() {{
                allowing(mockPropertyLayout).promptStyle();
                will(returnValue(null));

                allowing(mockFacetHolder).containsDoOpFacet(PromptStyleFacet.class);
                will(returnValue(true));

                never(mockConfiguration);
            }});

            PromptStyleFacet facet = PromptStyleFacetForPropertyLayoutAnnotation
                    .create(mockPropertyLayout, mockConfiguration, mockFacetHolder);

            Assert.assertThat(facet, is(nullValue()));
        }


    }

}
