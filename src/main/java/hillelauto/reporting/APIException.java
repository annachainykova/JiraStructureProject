/**
 * TestRail API_restassured binding for Java (API_restassured v2, available since TestRail 3.0)
 *
 * Learn more:
 *
 * http://docs.gurock.com/testrail-api2/start
 * http://docs.gurock.com/testrail-api2/accessing
 *
 * Copyright Gurock Software GmbH. See license.md for details.
 */

package hillelauto.reporting;
 
public class APIException extends Exception
{
	public APIException(String message)
	{
		super(message);
	}
}
