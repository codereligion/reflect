/**
 * Copyright 2012 www.codereligion.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codereligion.reflect;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isOneOf;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.codereligion.reflect.util.ApiUser;
import com.codereligion.reflect.util.ComplexClass;
import com.codereligion.reflect.util.MissingDefaultConstructor;
import com.codereligion.reflect.util.RestApi;
import com.codereligion.reflect.util.TypeMissmatchBetweenReadAndWriteMethods;
import com.codereligion.reflect.util.User;
import java.beans.PropertyDescriptor;
import java.util.Date;
import java.util.Set;
import org.junit.Test;

/**
 * Tests {@link Reflector}.
 * 
 * @author Sebastian Gröbler
 * @since 12.08.2012
 */
public class ReflectorTest {
	
	@Test(expected = NullPointerException.class)
	public void givenNullClassShouldThrowNpeWhenCallingHasDefaultConstructor() {
		Reflector.hasDefaultConstructor(null);
	}
	
	@Test(expected = NullPointerException.class)
	public void givenNullClassShouldThrowNpeWhenCallingGetWriteableProperties() {
		Reflector.getWriteableProperties(null);
	}
	
	@Test
	public void givenValidClassShouldReturnValidPropertieDescriptorsWhenCallingGetWriteableAndReadableProperties() {
		final Set<PropertyDescriptor> properties = Reflector.getWriteableAndReadableProperties(ComplexClass.class);
		
		assertNotNull(properties);
		assertFalse(properties.isEmpty());
		
		for (final PropertyDescriptor property : properties) {
			assertNotNull(property.getReadMethod());
			assertNotNull(property.getWriteMethod());
		}
	}

	@Test
	public void givenValidClassShouldReturnValidPropertieDescriptorsWhenCallingGetWriteableProperties() {
		final Set<PropertyDescriptor> properties = Reflector.getWriteableProperties(ComplexClass.class);
		
		assertNotNull(properties);
		assertFalse(properties.isEmpty());
		
		for (final PropertyDescriptor property : properties) {
			assertNotNull(property.getWriteMethod());
		}
	}
	
	@Test
	public void givenValidClassShouldReturnValidPropertieDescriptorsWhenCallingGetReadableProperties() {
		final Set<PropertyDescriptor> properties = Reflector.getReadableProperties(ComplexClass.class);
		
		assertNotNull(properties);
		assertFalse(properties.isEmpty());
		
		for (final PropertyDescriptor property : properties) {
			assertNotNull(property.getReadMethod());
		}
	}
	
	@Test
	public void givenUnboundGenericsClassShouldNotCauseIntrospectionBugWhenCallingGetWriteableAndReadableProperties() {
		final Set<PropertyDescriptor> properties = Reflector.getWriteableAndReadableProperties(User.class);
		
		assertNotNull(properties);
		assertFalse(properties.isEmpty());
		assertThat(properties.size(), is(1));
		
		final PropertyDescriptor property = properties.iterator().next();
		assertThat(property.getPropertyType(), is((Object)Integer.class));
		assertNotNull(property.getWriteMethod());
		assertNotNull(property.getReadMethod());
	}
	
	@Test
	public void givenUnboundGenericsClassShouldNotCauseIntrospectionBugWhenCallingGetWriteableProperties() {
		final Set<PropertyDescriptor> properties = Reflector.getWriteableProperties(User.class);
		
		assertNotNull(properties);
		assertFalse(properties.isEmpty());
		assertThat(properties.size(), is(1));
		
		final PropertyDescriptor property = properties.iterator().next();
		assertThat(property.getPropertyType(), is((Object)Integer.class));
		assertNotNull(property.getWriteMethod());
	}
	
	@Test
	public void givenUnboundGenericsClassShouldNotCauseIntrospectionBugWhenCallingGetReadableProperties() {
		final Set<PropertyDescriptor> properties = Reflector.getReadableProperties(User.class);
		
		assertNotNull(properties);
		assertFalse(properties.isEmpty());
		assertThat(properties.size(), is(2));
		
		for (final PropertyDescriptor property : properties) {
			assertThat(property.getPropertyType(), isOneOf((Object)Integer.class, (Object)Class.class));
			assertNotNull(property.getReadMethod());
		}
	}
	
	@Test
	public void givenBoundGenericsClassShouldNotCauseIntrospectionBugWhenCallingGetWriteableAndReadableProperties() {
		final Set<PropertyDescriptor> properties = Reflector.getWriteableAndReadableProperties(RestApi.class);
		
		assertNotNull(properties);
		assertFalse(properties.isEmpty());
		assertThat(properties.size(), is(1));
		
		final PropertyDescriptor property = properties.iterator().next();
		assertThat(property.getPropertyType(), is((Object)ApiUser.class));
		assertNotNull(property.getWriteMethod());
	}
	
	@Test
	public void givenBoundGenericsClassShouldNotCauseIntrospectionBugWhenCallingGetWriteableProperties() {
		final Set<PropertyDescriptor> properties = Reflector.getWriteableProperties(RestApi.class);
		
		assertNotNull(properties);
		assertFalse(properties.isEmpty());
		assertThat(properties.size(), is(1));
		
		final PropertyDescriptor property = properties.iterator().next();
		assertThat(property.getPropertyType(), is((Object)ApiUser.class));
		assertNotNull(property.getWriteMethod());
	}
	
	@Test
	public void givenBoundGenericsClassShouldNotCauseIntrospectionBugWhenCallingGetReadableProperties() {
		final Set<PropertyDescriptor> properties = Reflector.getReadableProperties(RestApi.class);
		
		assertNotNull(properties);
		assertFalse(properties.isEmpty());
		assertThat(properties.size(), is(2));
		
		for (final PropertyDescriptor property : properties) {
			assertThat(property.getPropertyType(), isOneOf((Object)ApiUser.class, (Object)Class.class));
			assertNotNull(property.getReadMethod());
		}
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void givenClassWithTypeMissmatchBetweenGetterAndSetterShouldThrowIaeWhenCallingGetWriteableAndReadableProperties() {
		final Set<PropertyDescriptor> properties = Reflector.getWriteableAndReadableProperties(TypeMissmatchBetweenReadAndWriteMethods.class);
		assertNotNull(properties);
		assertFalse(properties.isEmpty());
		assertThat(properties.size(), is(1));
		
		final PropertyDescriptor property = properties.iterator().next();
		assertThat(property.getPropertyType(), is((Object)Date.class));
		assertNotNull(property.getWriteMethod());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void givenClassWithTypeMissmatchBetweenGetterAndSetterShouldThrowIaeWhenCallingGetWriteableProperties() {
		final Set<PropertyDescriptor> properties = Reflector.getWriteableProperties(TypeMissmatchBetweenReadAndWriteMethods.class);
		assertNotNull(properties);
		assertFalse(properties.isEmpty());
		assertThat(properties.size(), is(1));
		
		final PropertyDescriptor property = properties.iterator().next();
		assertThat(property.getPropertyType(), is((Object)Date.class));
		assertNotNull(property.getWriteMethod());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void givenClassWithTypeMissmatchBetweenGetterAndSetterShouldThrowIaeWhenCallingGetReadableProperties() {
		final Set<PropertyDescriptor> properties = Reflector.getReadableProperties(TypeMissmatchBetweenReadAndWriteMethods.class);
		assertNotNull(properties);
		assertFalse(properties.isEmpty());
		assertThat(properties.size(), is(1));
		
		final PropertyDescriptor property = properties.iterator().next();
		assertThat(property.getPropertyType(), is((Object)Date.class));
		assertNotNull(property.getReadMethod());
	}
	
	@Test
	public void givenClassWithoutDefaultConstructorShouldReturnFalseWhenCallingHasDefaultConstructor() {
		assertFalse(Reflector.hasDefaultConstructor(MissingDefaultConstructor.class));
	}
	
	@Test
	public void givenClassWithDefaultConstructorShouldReturnWhenCallingHasDefaultConstructor() {
		assertTrue(Reflector.hasDefaultConstructor(ComplexClass.class));
	}
}
